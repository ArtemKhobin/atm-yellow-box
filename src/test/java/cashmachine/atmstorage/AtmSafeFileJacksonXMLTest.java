package cashmachine.atmstorage;

import cashmachine.money.MoneyPack;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class AtmSafeFileJacksonXMLTest {

  private MoneyPack mp1, mp2, mp3, mp4;
  private MoneyPack mpu1, mpu2, mpu3, mpu4, mpu5;
  private MoneyPack mpe1, mpe2, mpe3, mpe4;
  private ATMStorage atmStorage;

  @Before
  public void initialize() throws Exception {
    mp1 = new MoneyPack("USD", 500, 14);
    mp2 = new MoneyPack("USD", 100, 12);
    mp3 = new MoneyPack("USD", 50, 10);
    mp4 = new MoneyPack("USD", 10, 8);

    atmStorage = new ATMStorage("test");
  }

  @After
  public void destructor() throws Exception {
    File file = new File("safe-test.xml");
    if(file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testAtmSafe_XML_File_Test1() throws Exception {
    atmStorage.emptyStorage();
    atmStorage.store(mp1);
    atmStorage.store(mp2);
    atmStorage.store(mp3);
    atmStorage.store(mp4);

    AtmSafeFileJacksonXML xmlFile = new AtmSafeFileJacksonXML("safe-test.xml");
    xmlFile.saveSafe(atmStorage.getMoneyStorage());
    File file = new File("safe-test.xml");

    assertThat(file.exists(), is(true));

    HashMap<String, ArrayList<MoneyPack>> safe = xmlFile.loadSafe();

    assertThat(safe.isEmpty(), is(false));
    // USD
    assertThat(safe.get("USD").get(0).getAmount(), is(14));
    assertThat(safe.get("USD").get(1).getAmount(), is(12));
    assertThat(safe.get("USD").get(2).getAmount(), is(10));
    assertThat(safe.get("USD").get(3).getAmount(), is(8));
  }

}
