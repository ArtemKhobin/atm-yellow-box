package cashmachine.atmstorage;

import cashmachine.money.MoneyPack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class AtmSafeFileJackson implements AtmSafe {
  private String jsonFile = "safe.json";

  public void saveSafe(HashMap<String, ArrayList<MoneyPack>> safe) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    String safeStr = objectMapper.writeValueAsString(safe);

    try (PrintWriter out = new PrintWriter(jsonFile)) {
      out.println(safeStr);
    } catch (IOException exception) {
      System.out.println("Error open file: " + jsonFile + " " + exception);
    }
  }

  @SuppressWarnings("unchecked")
  public HashMap<String, ArrayList<MoneyPack>> loadSafe() throws IOException {
    HashMap<String, ArrayList<MoneyPack>> moneyStorage = new HashMap<>();
    String safeStr;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    File file = new File(jsonFile);
    if (!file.exists()) {
      saveSafe(moneyStorage);
      return moneyStorage;
    }

    try (BufferedReader in = new BufferedReader(new FileReader(jsonFile))) {
      safeStr = in.readLine();
      if (!safeStr.isEmpty()) {
        moneyStorage = objectMapper.readValue(safeStr, new TypeReference<HashMap<String, ArrayList<MoneyPack>>>() {});
      }
    } catch (IOException exception) {
      System.out.println("Error open file: " + jsonFile + " " + exception);
    }
    return moneyStorage;
  }
}
