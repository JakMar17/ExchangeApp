package si.fri.jakmar.exchangeapp.backend.testingutility.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.AssignmentEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionCorrectnessStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionEntity;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities.SubmissionSimilarityStatus;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.AssignmentEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.repositories.SubmissionEntityRepository;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.functions.SimilarityAlgorithms;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.SubmissionDto;
import si.fri.jakmar.exchangeapp.backend.testingutility.models.SubmissionSimilarityDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SimilarityTests {

    private final AssignmentEntityRepository assignmentEntityRepository;
    private final SubmissionEntityRepository submissionEntityRepository;

    @Value("${fileserver.path}")
    private String fileserverPath;

    public SubmissionDto[] testSimilarityOfSubmissions(Integer assignmentId, Integer[] submissionIds) throws DataNotFoundException {
        var submissions = new ArrayList<SubmissionEntity>();
        for(var id : submissionIds)
            submissions.add(
                    submissionEntityRepository.findById(id)
                            .orElseThrow(() -> new DataNotFoundException("Ne najdem oddaje s podanim IDjem"))
            );

        return testSimilarityOfSubmissions(
                assignmentEntityRepository.findById(assignmentId).orElseThrow(() -> new DataNotFoundException("Ne najdem naloge s podanim IDjem")),
                submissions
        );
    }

    public SubmissionDto[] testSimilarityOfSubmissions(AssignmentEntity assignment, List<SubmissionEntity> submissions) {
        var compareWithSubmissions = assignment.getSubmissions().stream()
                .filter(this::doTestWithSubmission)
                .toArray(SubmissionEntity[]::new);

        return submissions.parallelStream()
                .map(submission ->
                        SubmissionDto.createDtoWithSimilarityResult(
                                submission,
                                assignment,
                                testSimilarityOfSubmission(assignment, submission, compareWithSubmissions)
                        ))
                .toArray(SubmissionDto[]::new);
    }

    private SubmissionSimilarityDto[] testSimilarityOfSubmission (AssignmentEntity assignment, SubmissionEntity submission, SubmissionEntity[] compareTo) {
        return Arrays.stream(compareTo)
                .filter(e -> !e.equals(submission))
                .map(e -> compareSubmissions(assignment, submission, e))
                .filter(Objects::nonNull)
                .toArray(SubmissionSimilarityDto[]::new);
    }

    private SubmissionSimilarityDto compareSubmissions(AssignmentEntity assignment, SubmissionEntity compare, SubmissionEntity compareTo) {
        try {
            var s1Input = Files.readString(Path.of(fileserverPath + "input_" + compare.getFileKey()));
            var s1Output = Files.readString(Path.of(fileserverPath + "output_" + compare.getFileKey()));

            var s2Input = Files.readString(Path.of(fileserverPath + "input_" + compareTo.getFileKey()));
            var s2Output = Files.readString(Path.of(fileserverPath + "output_" + compareTo.getFileKey()));

            return new SubmissionSimilarityDto(
                    compareTo.getId(),
                    SimilarityAlgorithms.Companion.cosineSimilarity(s1Input, s2Input),
                    SimilarityAlgorithms.Companion.jaccardIndexSimilarity(s1Input, s2Input),
                    SimilarityAlgorithms.Companion.sorensenDiceSimilarity(s1Input, s2Input),
                    SimilarityAlgorithms.Companion.ratcliffObershelpSimilarity(s1Input, s2Input),

                    SimilarityAlgorithms.Companion.cosineSimilarity(s1Output, s2Output),
                    SimilarityAlgorithms.Companion.jaccardIndexSimilarity(s1Output, s2Output),
                    SimilarityAlgorithms.Companion.sorensenDiceSimilarity(s1Output, s2Output),
                    SimilarityAlgorithms.Companion.ratcliffObershelpSimilarity(s1Output, s2Output)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean doTestWithSubmission(SubmissionEntity submission) {
        if (submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.NOK ||
                submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.TIMEOUT ||
                submission.getCorrectnessStatus() == SubmissionCorrectnessStatus.COMPILE_ERROR)
            return false;
        else return submission.getSimilarityStatus() != SubmissionSimilarityStatus.NOK;
    }

}
