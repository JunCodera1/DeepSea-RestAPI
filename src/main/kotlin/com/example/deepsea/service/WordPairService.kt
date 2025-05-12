package com.example.deepsea.service

import com.example.deepsea.model.WordPair
import org.springframework.stereotype.Service

@Service
class WordPairService {
    // Mock data for word pairs (replace with database table and repository in production)
    private val wordPairs = listOf(
        // Unit 1: Introduce yourself
        WordPair(1, 1, "Hello", "こんにちは", "Konnichiwa"),
        WordPair(2, 1, "My name is", "私の名前は", "Watashi no namae wa"),
        WordPair(3, 1, "Nice to meet you", "はじめまして", "Hajimemashite"),
        // Unit 2: Greet others
        WordPair(4, 2, "Good morning", "おはよう", "Ohayou"),
        WordPair(5, 2, "Good evening", "こんばんは", "Konbanwa"),
        WordPair(6, 2, "How are you?", "お元気ですか？", "Ogenki desu ka?"),
        // Unit 6: Talk about daily routines
        WordPair(7, 6, "I wake up", "私は起きる", "Watashi wa okiru"),
        WordPair(8, 6, "I eat breakfast", "私は朝食を食べる", "Watashi wa choushoku o taberu"),
        WordPair(9, 6, "I go to work", "私は仕事に行く", "Watashi wa shigoto ni iku"),
        // Unit 11: Order food
        WordPair(10, 11, "I want rice", "ご飯が欲しい", "Gohan ga hoshii"),
        WordPair(11, 11, "Water, please", "水をください", "Mizu o kudasai"),
        WordPair(12, 11, "Can you make it quick?", "早くできますか？", "Hayaku dekimasu ka?"),
        // Unit 16: Ask for directions
        WordPair(13, 16, "Where is the station?", "駅はどこですか？", "Eki wa doko desu ka?"),
        WordPair(14, 16, "Go straight", "まっすぐ行く", "Massugu iku"),
        WordPair(15, 16, "Turn left", "左に曲がる", "Hidari ni magaru")
        // Add more word pairs for other units as needed
    )

    fun getRandomWordPairs(sectionId: Long, unitId: Long, count: Int = 5): List<WordPair> {
        // Filter word pairs by unitId
        val unitPairs = wordPairs.filter { it.unitId == unitId }
        // Return random selection, ensuring we don't exceed available pairs
        return unitPairs.shuffled().take(count.coerceAtMost(unitPairs.size))
    }
}