package com.example.cis_436_project_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cis_436_project_1.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dieVal = 1
    private var num1 = 0
    private var num2 = 0
    private var correctAnswer = 0
    private var jackpot = 5
    var player2Turn = false // false = player 1 turn, true = player 2 turn
    var waitForAnswer = false // false = last player finished problem and new player hasn't yet rolled for a problem,
                              // true = player has rolled for a problem but not answered yet
    var waitForRoll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //event handler for rolling the die
        //when we roll the die we change the die image, figure out what problem to do
        //and display it
        binding.btnRoll.setOnClickListener {
            if(waitForAnswer){return@setOnClickListener }
            dieVal = Random.nextInt(1, 4)
            var image = "@drawable/dice" + dieVal
            var resourceID = resources.getIdentifier(image, "drawable", getPackageName())
            binding.ivDie.setImageResource(resourceID)

            when(dieVal){

                1->{num1 = Random.nextInt(1,100)
                    num2 = Random.nextInt(1,100)
                    correctAnswer = num1 + num2
                    binding.tvQuestion.text = num1.toString() + " + " + num2.toString() +" = "
                }

                2->{num1 = Random.nextInt(1,100)
                    num2 = Random.nextInt(1,100)
                    if (num1<num2){
                        var temp = num1
                        num1 = num2
                        num2 = temp
                    }
                    correctAnswer = num1 - num2
                    binding.tvQuestion.text = num1.toString() + " - " + num2.toString() +" = "
                }

                3->{num1 = Random.nextInt(1,21)
                    num2 = Random.nextInt(1,21)
                    correctAnswer = num1 * num2
                    binding.tvQuestion.text = num1.toString() + " * " + num2.toString() +" = "
                }

                //4-> rerollfordoublepoints()

                //5-> lostturn()

                //6-> tryforjackpot()
            }
            waitForAnswer = true
            waitForRoll = false
        }

        //event handler for entering the answer
        binding.btnEnter.setOnClickListener{
            if (waitForRoll){return@setOnClickListener}
            var userAns = binding.etAnswer.text.toString().toInt()
            var newScore = 0
            waitForAnswer = false
            waitForRoll = true

            when(dieVal){
                in 1..3-> newScore = dieVal
                //4-> newScore = 2 * dieVal
                //5-> null
                //6-> newScore = jackpot

            }

            if (userAns==correctAnswer){
                if (player2Turn){
                    binding.tvScore2.text = (binding.tvScore2.text.toString().toInt() + newScore).toString()
                }
                else{
                    binding.tvScore1.text = (binding.tvScore1.text.toString().toInt() + newScore).toString()
                }
            }
            else{

                jackpot += dieVal
                binding.tvJackpot.text = "Jackpot = " + jackpot
            }

            player2Turn = !player2Turn
            if (player2Turn){
                binding.tvPlayerTurn.text = "Player 2's Turn"
            }
            else{
                binding.tvPlayerTurn.text = "Player 1's Turn"
            }
        }




    }
}