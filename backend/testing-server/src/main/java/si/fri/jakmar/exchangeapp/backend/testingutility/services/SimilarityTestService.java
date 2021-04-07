package si.fri.jakmar.exchangeapp.backend.testingutility.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.*;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.AssignmentEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.SubmissionEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.SubmissionSimilarityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.SimilarityAlgorithms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimilarityTestService {

    private final SubmissionSimilarityRepository submissionSimilarityRepository;
    private final SubmissionEntityRepository submissionEntityRepository;
    private final AssignmentEntityRepository assignmentEntityRepository;
    @Value("${fileserver.path}")
    private String fileserverPath;

    public SubmissionSimilarityEntity[] calculateSimilarityForAssignment(Integer assignmentId) throws DataNotFoundException {
        return calculateSimilarityForAssignment(assignmentEntityRepository.findById(assignmentId).orElseThrow(() -> new DataNotFoundException("Ne najdem naloge")));
    }

    public SubmissionSimilarityEntity[] calculateSimilarityForAssignment(AssignmentEntity assignmentEntity) {
        var similarities = assignmentEntity.getSubmissions().parallelStream()
                .filter(e -> e.getCorrectnessStatus() == SubmissionCorrectnessStatus.OK)
                .filter(e -> e.getSimilarities() == null || e.getSimilarities().size() == 0)
                .map(this::calculateSimilarityForSubmission)
                .flatMap(Collection::stream)
                .toArray(SubmissionSimilarityEntity[]::new);
        submissionSimilarityRepository.saveAll(Arrays.asList(similarities));

        var submissions = submissionEntityRepository.findByAssignment(assignmentEntity).parallelStream()
                .filter(e -> e.getSimilarityStatus() == null || e.getSimilarityStatus() == SubmissionSimilarityStatus.PENDING_REVIEW)
                .map(this::setSimilarityStatus)
                .toArray(SubmissionEntity[]::new);
        submissionEntityRepository.saveAll(Arrays.asList(submissions));

        System.out.println(similarities.length);
        return similarities;
    }

    public List<SubmissionSimilarityEntity> calculateSimilarityForSubmission(SubmissionEntity submission) {
        var assignment = submission.getAssignment();
        var submissions = assignment.getSubmissions();

        return submissions.stream()
                .filter(e -> e.getCorrectnessStatus() == SubmissionCorrectnessStatus.OK)
                .filter(e -> !e.equals(submission))
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

    private SubmissionEntity setSimilarityStatus(SubmissionEntity entity) {
        var maxSimilaritiy = entity.getSimilarities().stream()
                .map(SubmissionSimilarityEntity::getAverage)
                .max(Comparator.naturalOrder()).map(e -> e * 100).orElse(null);

        System.out.println(maxSimilaritiy + "    " + entity.getAssignment().getPlagiarismLevel());

        SubmissionSimilarityStatus status;
        if (maxSimilaritiy == null || maxSimilaritiy >= entity.getAssignment().getPlagiarismLevel())
            status = SubmissionSimilarityStatus.NOK;
        else if (maxSimilaritiy >= entity.getAssignment().getPlagiarismWarning())
            status = SubmissionSimilarityStatus.WARNING;
        else
            status = SubmissionSimilarityStatus.OK;

        entity.setSimilarityStatus(status);
        return entity;
    }
}
