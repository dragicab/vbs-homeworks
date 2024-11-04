package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;

import java.io.FileInputStream;
import java.io.IOException;


public class nova {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();


        String filePath = "C:\\Users\\mirch\\Downloads\\kol2023-g1-main_obid (2)\\vbs_dom1\\nTriples.nt";

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            model.read(inputStream, null, "N-TRIPLES");
            System.out.println("RDF графот е успешно вчитан од " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }



        String yourFullName = "Dragica Boshnakova"; // Променете го во вашето реално име
        Resource yourResource = model.listSubjectsWithProperty(VCARD.FN, yourFullName)
                .nextResource();

        // Проверка и печатење на ресурсот
        if (yourResource != null) {
            System.out.println("\nSelected resource representing you:");
            System.out.println(yourResource);

            // Читање на вредности од ресурсот
            // Прочитање на целосно име
            if (yourResource.hasProperty(VCARD.FN)) {
                Literal fullName = yourResource.getProperty(VCARD.FN).getObject().asLiteral();
                System.out.println("Full Name: " + fullName.getString());
            }

            // Прочитање на име (ако е додадено)
            if (yourResource.hasProperty(VCARD.Given)) {
                Literal firstName = yourResource.getProperty(VCARD.Given).getObject().asLiteral();
                System.out.println("First Name: " + firstName.getString());
            }

            // Прочитање на презиме (ако е додадено)
            if (yourResource.hasProperty(VCARD.Family)) {
                Literal lastName = yourResource.getProperty(VCARD.Family).getObject().asLiteral();
                System.out.println("Last Name: " + lastName.getString());
            }
        } else {
            System.out.println("Ресурсот не е пронајден.");
        }


//        System.out.println("Printing RDF triples from the loaded model:");
//        model.listStatements().forEachRemaining(statement -> {
//            String subject = statement.getSubject().toString();
//            String predicate = statement.getPredicate().toString();
//            String object = statement.getObject().isResource() ?
//                    statement.getObject().toString() :
//                    "\"" + statement.getObject().toString() + "\"";
//            System.out.println(subject + " - " + predicate + " - " + object);
//        });
    }
}
