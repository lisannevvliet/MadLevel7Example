package com.example.madlevel7example.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7example.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuizRepository {

    // Make sure we have an instance of Firestore.
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    // Reference to the document that we need to store/retrieve our quiz.
    private var quizDocument = firestore.collection("quizzes").document("quiz")

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()

    val quiz: LiveData<Quiz>
    get() = _quiz

    // The CreateQuizFragment can use this to see if creation succeeded.
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
    get() = _createSuccess

    suspend fun getQuiz() {
        try {
            // Firestore has support for coroutines via the extra dependency we've added.
            withTimeout(5000) {
                val data = quizDocument.get().await()

                val question = data.getString("question").toString()
                val answer = data.getString("answer").toString()

                _quiz.value = Quiz(question, answer)
            }
        } catch (e: Exception) {
            throw QuizRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun createQuiz(quiz: Quiz) {
        // Persist data to Firestore.
        try {
            // Firestore has support for coroutines via the extra dependency we've added.
            withTimeout(5000) {
                quizDocument.set(quiz)

                _createSuccess.value = true
            }
        } catch (e: Exception) {
            throw QuizSaveError(e.message.toString(), e)
        }
    }

    class QuizSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class QuizRetrievalError(message: String) : Exception(message)
}