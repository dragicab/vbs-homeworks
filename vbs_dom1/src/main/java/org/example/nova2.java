package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.jena.vocabulary.AS.object;
import static org.apache.jena.vocabulary.RDF.Nodes.predicate;

public class nova2 {
    public static void main(String[] args) {
        // Load the model
        Model model = ModelFactory.createDefaultModel();

        String filePath = "C:\\Users\\mirch\\Downloads\\kol2023-g1-main_obid (2)\\vbs_dom1\\datoteka.ttl";

        try {
            model.read(new FileInputStream(filePath), null, "TURTLE");

            String medicineURI = "http://purl.org/net/hifm/data#979287"; // URI на одбраниот лек
            Resource medicine = model.getResource(medicineURI);
            Resource drugType = model.getResource("http://purl.org/net/hifm/ontology#Drug");
            ResIterator resIterator = model.listResourcesWithProperty(RDF.type, drugType);

            if (!resIterator.hasNext()) {
                System.out.println("No resources of type 'Drug' found. Please check the URI or dataset.");
                return;
            }

            Resource selectedMedicine = resIterator.nextResource();
            String selectedMedicineName = selectedMedicine.getProperty(RDFS.label) != null
                    ? selectedMedicine.getProperty(RDFS.label).getString()
                    : "Нема достапно име";

            // Извлекување на цената на лекот
            Literal priceLiteral = selectedMedicine.getProperty(model.getProperty("http://purl.org/net/hifm/ontology#refPriceWithVAT")) != null
                    ? selectedMedicine.getProperty(model.getProperty("http://purl.org/net/hifm/ontology#refPriceWithVAT")).getLiteral()
                    : null;

            String price = (priceLiteral != null) ? priceLiteral.getString() : "Нема достапна цена";

            // Печатење на именото и цената на одбраниот лек
            System.out.println("Избран лек: " + selectedMedicineName);
            System.out.println("Цена: "+ price + "денари");
            // Извлекување на слични лекови
            Property similarTo = model.getProperty("http://purl.org/net/hifm/ontology#similarTo");
            StmtIterator stmtIterator = selectedMedicine.listProperties(similarTo);

            Set<String> similarMedicines = new HashSet<>();
            Set<String> similarPrices = new HashSet<>();

            while (stmtIterator.hasNext()) {
                Statement stmt = stmtIterator.nextStatement();
                Resource similarMedicine = stmt.getObject().asResource();
                String similarMedicineName = similarMedicine.getProperty(RDFS.label) != null
                        ? similarMedicine.getProperty(RDFS.label).getString()
                        : "Нема достапно име";

                // Извлекување на цената на сличниот лек
                Literal similarPriceLiteral = similarMedicine.getProperty(model.getProperty("http://purl.org/net/hifm/ontology#refPriceWithVAT")) != null
                        ? similarMedicine.getProperty(model.getProperty("http://purl.org/net/hifm/ontology#refPriceWithVAT")).getLiteral()
                        : null;

                String similarPrice = (similarPriceLiteral != null) ? similarPriceLiteral.getString() : "Нема достапна цена";

                similarMedicines.add(similarMedicineName);
                similarPrices.add(similarPrice);
            }

            // Печатење на слични лекови
            System.out.println("Слични лекови и цени:");
            if (similarMedicines.isEmpty()) {
                System.out.println("Нема пронајдено сличности.");
            } else {
                int index = 0;
                for (String name : similarMedicines) {
                    System.out.println(" - " + name + " - цена: " + (similarPrices.toArray()[index++] != null ? similarPrices.toArray()[index - 1] : "Нема достапна цена") + " денари");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the Turtle file.");
        }
    }
}