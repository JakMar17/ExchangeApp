package si.fri.jakmar.backend.exchangeapp.services.submissions;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InputOutputPairWrapper {
    private final MultipartFile input;
    private final MultipartFile output;

    public InputOutputPairWrapper(MultipartFile input, MultipartFile output) {
        this.input = input;
        this.output = output;
    }

    public MultipartFile getInput() {
        return input;
    }

    public MultipartFile getOutput() {
        return output;
    }

    public static List<InputOutputPairWrapper> generateWrapperListFromInputsOutputs(List<MultipartFile> inputs, List<MultipartFile> outputs) throws FileException {
        var list = new LinkedList<InputOutputPairWrapper>();

        for(var input : inputs) {
            var inputFileCode = Objects.requireNonNull(input.getOriginalFilename())
                    .split("\\.")[0]
                    .split("vhod")[1];

            var output = CollectionUtils.emptyIfNull(outputs)
                    .stream()
                    .filter(e ->
                            Objects.requireNonNull(e.getOriginalFilename())
                                    .split("\\.")[0]
                                    .split("izhod")[1]
                                .equals(inputFileCode)
                    )
                    .findFirst().orElseThrow(() -> new FileException(
                            String.format("Vhodna datoteka %s nima izhodnega para", inputFileCode)
                    ));

            list.add(new InputOutputPairWrapper(input, output));
        }

        return list;
    }
}
