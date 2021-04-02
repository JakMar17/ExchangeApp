package si.fri.jakmar.exchangeapp.backend.testingutility.functions

import info.debatty.java.stringsimilarity.Cosine
import info.debatty.java.stringsimilarity.Jaccard
import info.debatty.java.stringsimilarity.RatcliffObershelp
import info.debatty.java.stringsimilarity.SorensenDice

abstract class SimilarityAlgorithms {
    companion object {
        fun cosineSimilarity(string1: String, string2: String): Double {
            val cosine = Cosine();
            return cosine.similarity(string1, string2);
        }

        fun jaccardIndexSimilarity(string1: String, string2: String): Double {
            val jaccard = Jaccard();
            val data = jaccard.similarity(string1, string2);
            return data
        }

        fun ratcliffObershelpSimilarity(string1: String, string2: String): Double {
            val ratcliffObershelp = RatcliffObershelp();
            return ratcliffObershelp.similarity(string1, string2);
        }

        fun sorensenDiceSimilarity(string1: String, string2: String): Double {
            val sorensenDice = SorensenDice();
            return sorensenDice.similarity(string1, string2);
        }
    }
}