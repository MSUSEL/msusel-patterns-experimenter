package edu.montana.gsoc.msusel.patterns;

import edu.montana.gsoc.msusel.patterns.datamodel.Pattern;

import javax.persistence.EntityManager;
import java.util.List;

public class PatternsJPA {

    public static void main(String args[]) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        // Check database version
        String sql = "select version()";


//        String[] names = {
//                "Singleton",
//                "Factory Method",
//                "Prototype",
//                "(Object)Adapter",
//                "Command",
//                "Composite",
//                "Decorator",
//                "Observer",
//                "State",
//                "Strategy",
//                "Bridge",
//                "Template Method",
//                "Visitor",
//                "Proxy",
//                "Proxy2",
//                "Chain of Responsibility"
//        };
//
//        for (String n : names) {
//            Pattern pattern = Pattern.builder()
//                    .name(n)
//                    .create();
//
//            entityManager.persist(pattern);
//        }

        entityManager.getTransaction().commit();

        List<Pattern> patterns = entityManager.createQuery("select p from Pattern p").getResultList();

        patterns.forEach(System.out::println);
        JPAUtil.shutdown();
    }
}
