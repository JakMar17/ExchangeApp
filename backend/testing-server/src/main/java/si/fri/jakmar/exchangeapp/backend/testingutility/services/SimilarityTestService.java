package si.fri.jakmar.exchangeapp.backend.testingutility.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionSimilarityEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.AssignmentEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.SubmissionSimilarityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.SimilarityAlgorithms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimilarityTestService {

    @Value("${fileserver.path}")
    private String fileserverPath;

    private final SubmissionSimilarityRepository submissionSimilarityRepository;
    private final AssignmentEntityRepository assignmentEntityRepository;

    public SubmissionSimilarityEntity[] calculateSimilarityForAssignment(Integer assignmentId) throws DataNotFoundException {
        return calculateSimilarityForAssignment(assignmentEntityRepository.findById(assignmentId).orElseThrow(() -> new DataNotFoundException("Ne najdem naloge")));
    }

    public SubmissionSimilarityEntity[] calculateSimilarityForAssignment(AssignmentEntity assignmentEntity) {
        var similarities =  assignmentEntity.getSubmissions().parallelStream()
                .filter(e -> e.getStatus() == SubmissionStatus.OK)
                .filter(e -> e.getSimilarities() == null || e.getSimilarities().size() == 0)
                .map(this::calculateSimilarityForSubmission)
                .flatMap(Collection::stream)
                .toArray(SubmissionSimilarityEntity[]::new);

        submissionSimilarityRepository.saveAll(Arrays.asList(similarities));
        System.out.println(similarities.length);
        return similarities;
    }

    public List<SubmissionSimilarityEntity> calculateSimilarityForSubmission(SubmissionEntity submission) {
        var assignment = submission.getAssignment();
        var submissions = assignment.getSubmissions();

        return submissions.stream()
                .filter(e -> e.getStatus() == SubmissionStatus.OK)
                .map(e -> {
                    try {
                        SubmissionSimilarityEntity similarity = calculateOnePairOfSubmissions(submission, e, assignment);
                        System.out.println("delam");
//                        return submissionSimilarityRepository.save(similarity.beforeSavingToMongo());
                        return similarity.beforeSavingToMongo();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                //.toArray(SubmissionSimilarityEntity[]::new);
                .collect(Collectors.toList());
    }


    private SubmissionSimilarityEntity calculateOnePairOfSubmissions(SubmissionEntity s1, SubmissionEntity s2, AssignmentEntity assignment) throws IOException {
            var s1Input = Files.readString(Path.of(fileserverPath + "input_" + s1.getFileKey()));
            var s1Output = Files.readString(Path.of(fileserverPath + "output_" + s1.getFileKey()));

            var s2Input = Files.readString(Path.of(fileserverPath + "input_" + s2.getFileKey()));
            var s2Output = Files.readString(Path.of(fileserverPath + "output_" + s2.getFileKey()));


        return new SubmissionSimilarityEntity(
                SimilarityAlgorithms.Companion.cosineSimilarity(s1Input, s2Input), SimilarityAlgorithms.Companion.jaccardIndexSimilarity(s1Input, s2Input), SimilarityAlgorithms.Companion.sorensenDiceSimilarity(s1Input, s2Input), SimilarityAlgorithms.Companion.ratcliffObershelpSimilarity(s1Input, s2Input),
                SimilarityAlgorithms.Companion.cosineSimilarity(s1Output, s2Output), SimilarityAlgorithms.Companion.jaccardIndexSimilarity(s1Output, s2Output), SimilarityAlgorithms.Companion.sorensenDiceSimilarity(s1Output, s2Output), SimilarityAlgorithms.Companion.ratcliffObershelpSimilarity(s1Output, s2Output),
                s1, s2
        );
    }
}
