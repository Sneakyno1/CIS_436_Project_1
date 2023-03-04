package com.example.cis_436_project_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cis_436_project_1.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    // the following 3 functions are just for generating questions
    fun addition(){
        num1 = Random.nextInt(1, 100)
        num2 = Random.nextInt(1, 100)
        correctAnswer = num1 + num2
        binding.tvQuestion.text = num1.toString() + " + " + num2.toString() + " = "
    }
    fun subtraction(){
        num1 = Random.nextInt(1, 100)
        num2 = Random.nextInt(1, 100)
        if (num1 < num2) {
            var temp = num1
            num1 = num2
            num2 = temp
        }
        correctAnswer = num1 - num2
        binding.tvQuestion.text = num1.toString() + " - " + num2.toString() + " = "
    }
    fun multiplication(){
        num1 = Random.nextInt(1, 21)
        num2 = Random.nextInt(1, 21)
        correctAnswer = num1 * num2
        binding.tvQuestion.text = num1.toString() + " * " + num2.toString() + " = "
    }

    private lateinit var binding: ActivityMainBinding

    private var originalDieValue = 1
    private var reRollDieValue = 1

    private var num1 = 0
    private var num2 = 0

    private var correctAnswer = 0

    private var jackpot = 5


    var waitForAnswer = false // false = last player finished problem and new player hasn't yet rolled for a problem,
                              // true = player has rolled for a problem but not answered yet
    var waitForRoll = true    // similar to waitForAnswer except with respect to the enter answer button
    var reRoll = false

    var player2Turn = false // false = player 1 turn, true = player 2 turn
    var gameWon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //event handler for rolling the die
        binding.btnRoll.setOnClickListener {
            var image = "@drawable/dice" + originalDieValue
            var resourceID = resources.getIdentifier(image, "drawable", getPackageName())

            //Blocks user from continuously re rolling the die
            if (waitForAnswer) {
                return@setOnClickListener
            }

            //guard clause for when we need to re roll the die if they get a 4 or 6 first
            if (reRoll) {
                reRollDieValue = Random.nextInt(1, 4)
                image = "@drawable/dice" + reRollDieValue
                resourceID = resources.getIdentifier(image, "drawable", getPackageName())
                binding.ivDie.setImageResource(resourceID)

            when (reRollDieValue) {

                1 -> addition()

                2 -> subtraction()

                3 -> multiplication()
            }
                waitForAnswer = true
                waitForRoll = false
                reRoll = false
                return@setOnClickListener
            }

            //main body for normal presses
            originalDieValue = Random.nextInt(1, 7)
            image = "@drawable/dice" + originalDieValue
            resourceID = resources.getIdentifier(image, "drawable", getPackageName())
            binding.ivDie.setImageResource(resourceID)

            when (originalDieValue) {
                //give them an addition problem
                1 -> addition()
                //give them a subtraction problem
                2 -> subtraction()
                //give them a multiplication problem
                3 -> multiplication()
                //re roll for double points
                4-> { reRoll=true
                    binding.tvPlayerTurn.text = "Press the roll button \nagain to try for a \nquestion worth \ndouble points!"
                    return@setOnClickListener
                }
                //current player loses turn
                5-> {
                    if (player2Turn){
                        binding.tvPlayerTurn.text = "Player 2 lost a turn"
                        player2Turn = !player2Turn
                        binding.tvQuestion.text = "Player 1 roll"
                        return@setOnClickListener
                    }
                    binding.tvPlayerTurn.text = "Player 1 lost a turn"
                    player2Turn = !player2Turn
                    binding.tvQuestion.text = "Player 2 roll"
                    return@setOnClickListener
                }
                //player tries for the jackpot
                6-> { reRoll=true
                    binding.tvPlayerTurn.text = "Press the roll button again \nto try for the jackpot!"
                    return@setOnClickListener
                }
            }
            waitForAnswer = true
            waitForRoll = false
        }

        //event handler for entering the answer
        binding.btnEnter.setOnClickListener {
            //prevents button from being pressed if we need the player to roll the die
            if (waitForRoll) {
                return@setOnClickListener
            }

            if (gameWon){
                originalDieValue = 1
                reRollDieValue = 1
                num1 = 0
                num2 = 0
                correctAnswer = 0
                jackpot = 5
                player2Turn = false
                waitForAnswer = false
                waitForRoll = true
                reRoll = false
                gameWon = false
                binding.tvPlayerTurn.text = "Player 1's turn"
                binding.tvScore1.text = "0"
                binding.tvScore2.text = "0"
                binding.etAnswer.text.clear()
                binding.tvQuestion.text = "Player 1 roll"
                return@setOnClickListener
            }
            var userAns = binding.etAnswer.text.toString().toInt()
            var addToScore = 0

            waitForAnswer = false
            waitForRoll = true

            //adds score based on the value rolled for the die, so failed double points questions will
            //count for 4 points, and failed jackpots count for 6
            when (originalDieValue) {
                in 1..3 -> addToScore = originalDieValue
                4-> addToScore = 2 * reRollDieValue
                5-> return@setOnClickListener
                6-> addToScore = jackpot

            }

            if (userAns == correctAnswer) {
                if (originalDieValue==6){
                    jackpot = 5
                    binding.tvJackpot.text = jackpot.toString()
                }

                if (player2Turn) {
                    binding.tvScore2.text =
                        (binding.tvScore2.text.toString().toInt() + addToScore).toString()
                } else {
                    binding.tvScore1.text =
                        (binding.tvScore1.text.toString().toInt() + addToScore).toString()
                }
            } else {

                jackpot += originalDieValue
                binding.tvJackpot.text = "Jackpot = " + jackpot
            }

            if (binding.tvScore1.text.toString().toInt() >= 20){
                binding.tvPlayerTurn.text = "Player 1 won the game!\n Press the answer button for a new game!"
                gameWon = true
                waitForAnswer = true
                waitForRoll = false
                return@setOnClickListener
            }
            if (binding.tvScore2.text.toString().toInt() >= 20){
                binding.tvPlayerTurn.text = "Player 2 won the game!\n Press the answer button for a new game!"
                gameWon = true
                waitForAnswer = true
                waitForRoll = false
                return@setOnClickListener
            }

            player2Turn = !player2Turn
            if (player2Turn) {
                binding.tvPlayerTurn.text = "Player 2's Turn"
                binding.tvQuestion.text = "Player 2 roll"
                binding.etAnswer.text.clear()
            } else {
                binding.tvPlayerTurn.text = "Player 1's Turn"
                binding.tvQuestion.text = "Player 1 roll"
                binding.etAnswer.text.clear()
            }
            

        }


    }
}