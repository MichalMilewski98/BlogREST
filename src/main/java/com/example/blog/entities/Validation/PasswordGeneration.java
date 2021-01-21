package com.example.blog.entities.Validation;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import java.util.Arrays;
import java.util.List;


public class PasswordGeneration {

    public static String generatePassword()
    {
        List<CharacterRule> rules = Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1));
       PasswordGenerator passwordGenerator = new PasswordGenerator();
       String password = passwordGenerator.generatePassword(20, rules);

       return password;
    }

}
