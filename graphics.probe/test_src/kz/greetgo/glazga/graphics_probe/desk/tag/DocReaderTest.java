package kz.greetgo.glazga.graphics_probe.desk.tag;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DocReaderTest {

  @Test
  public void read() throws Exception {
    Doc doc = DocReader.read("Asd a 123, b 11 { Lex x 31.23, y 1 {} Lex x 11.23, y -123.78 { A {} } } ");

    assertThat(doc.first()).isNotNull();
    assertThat(doc.first().name()).isEqualTo("Asd");
    assertThat(doc.first().attr("a")).isEqualTo("123");
    assertThat(doc.first().attr("b")).isEqualTo("11");
    assertThat(doc.first().children().get(0).name()).isEqualTo("Lex");
    assertThat(doc.first().children().get(1).name()).isEqualTo("Lex");
    assertThat(doc.first().children().get(1).attr("y")).isEqualTo("-123.78");

    assertThat(doc.first().children().get(0).children()).isEmpty();
    assertThat(doc.first().children().get(1).children()).isNotEmpty();

    System.out.println(doc.toString());
  }
}
