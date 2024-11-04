package org.example;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

public class Main {

    public static void main(String[] args) {
        // Креирање на основен Jena модел
        Model model = ModelFactory.createDefaultModel();

        // URI на вашиот социјален профил
        String uri = "https://www.instagram.com/bosnakovad/";

        // Додавање на нов ресурс кој ве репрезентира вас
        Resource person = model.createResource(uri)
                .addProperty(RDF.type, VCARD.FN)
                .addProperty(VCARD.FN, "Dragica Boshnakova")
                .addProperty(VCARD.NICKNAME, "Daci")
                .addProperty(VCARD.EMAIL, "dragica.boshnakova@students.finki.ukim.mk")
                .addProperty(VCARD.TEL, "тел:+38970123456")
                .addProperty(VCARD.BDAY, "2001-12-23")
                .addProperty(FOAF.age, model.createTypedLiteral(22))
                .addProperty(FOAF.based_near, "Skopje, Macedonia")
                .addProperty(FOAF.gender, "Female")
                .addProperty(model.createProperty("http://example.org#favoriteColor"), "Pink")
                .addProperty(model.createProperty("http://example.org#education"), "FINKI") ;

        Resource friend = model.createResource("https://www.instagram.com/olgicaupcheva/")
                .addProperty(FOAF.name, "Olgica Upcheva");

        person.addProperty(FOAF.knows, friend);
        Resource friend2 = model.createResource("https://www.instagram.com/r.temelkoska/")
                .addProperty(FOAF.name, "Rosica Temelkoska");

        person.addProperty(FOAF.knows, friend2);

        // (Опционално) Испишување на моделот за да го видите неговиот тековен статус
        model.write(System.out, "Turtle");

        System.out.println("Printing with model.listStatements():");
        model.listStatements().forEachRemaining(statement -> {
            String subject = statement.getSubject().toString();
            String predicate = statement.getPredicate().toString();
            String object = statement.getObject().isResource() ?
                    statement.getObject().toString() :
                    "\"" + statement.getObject().toString() + "\"";
            System.out.println(subject + " - " + predicate + " - " + object);
        });

        // RDF/XML
        System.out.println("Printing with model.write(), in RDF/XML:");
        model.write(System.out, "RDF/XML");

        // Pretty RDF/XML
        System.out.println("Printing with model.write(), in Pretty RDF/XML:");
        model.write(System.out, "RDF/XML-ABBREV");

        // N-Triples
        System.out.println("Printing with model.write(), in N-Triples:");
        model.write(System.out, "N-TRIPLES");

        // Turtle
        System.out.println("Printing with model.write(), in Turtle:");
        model.write(System.out, "TURTLE");
    }
}

