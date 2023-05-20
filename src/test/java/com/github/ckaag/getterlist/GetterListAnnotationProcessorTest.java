package com.github.ckaag.getterlist;

import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.elementary.junit.annotations.Processors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(JavacExtension.class)
//@Options("-Werror")
@Processors({GetterListAnnotationProcessor.class})
@Inline(name = "ExampleDto", source =
        "\n" +
                "package com.github.ckaag.getterlist.testcase;\n" +
                "\n" +
                "import lombok.experimental.FieldNameConstants;\n" +
                "\n" +
                "@FieldNameConstants\n" +
                "public class ExampleDto {\n" +
                "    private long id;\n" +
                "    private String name;\n" +
                "    private boolean active;\n" +
                "\n" +
                "    public long getId() {\n" +
                "        return id;\n" +
                "    }\n" +
                "\n" +
                "    public void setId(long id) {\n" +
                "        this.id = id;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "\n" +
                "    public void setName(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public boolean isActive() {\n" +
                "        return active;\n" +
                "    }\n" +
                "\n" +
                "    public void setActive(boolean active) {\n" +
                "        this.active = active;\n" +
                "    }\n" +
                "}\n" +
                "                      ")
//@Classpath("com.github.ckaag.getterlist.testcase.ExampleDto")
class GetterListAnnotationProcessorTest {
    @Test
    void process_fields(Results results) throws IOException {
        assertEquals(0, results.find().errors().count());
        assertEquals(1, results.generated.size());

        var generatedSourceCode = "";
        try (Reader reader = results.generated.get(0).openReader(true)) {
            generatedSourceCode = readAllFrom(reader);
        }

        String expectedSourceCode = "error";
        try (var inputStream = new FileInputStream("src/test/java/com/github/ckaag/getterlist/testcase/ExampleDtoGetterList.java")) {
            expectedSourceCode = readFromInputStream(inputStream);
        }

        assertEquals(expectedSourceCode.lines().collect(Collectors.toList()), generatedSourceCode.lines().collect(Collectors.toList()));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append(System.lineSeparator());
            }
        }
        return resultStringBuilder.toString();
    }

    private String readAllFrom(Reader reader) throws IOException {
        int intValueOfChar;
        StringBuilder targetString = new StringBuilder();
        while ((intValueOfChar = reader.read()) != -1) {
            targetString.append((char) intValueOfChar);
        }
        return targetString.toString();
    }
}