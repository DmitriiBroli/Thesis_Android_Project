package com.app.greenpass.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.greenpass.activities.login.LoggedInActivity
import com.app.greenpass.activities.persondb.DatabasePersonActivity
import com.app.greenpass.activities.testdb.DatabaseTestActivity
import com.app.greenpass.activities.vaccdb.DatabaseVaccinationActivity
import com.app.greenpass.databinding.ActivityMainBinding
import kotlinx.coroutines.*


//the first activity you see upon starting the app,
open class MainActivity : AppCompatActivity() {
    //use binding for more efficient layout control
    private lateinit var mBinding: ActivityMainBinding
    private val binding get() = mBinding
    private val handler = CoroutineExceptionHandler{_, exception ->
        if (Looper.myLooper() == null) {
            Looper.prepare()
            Looper.loop()
        }
        Log.i("Coroutine:", "Error", exception)
        Toast.makeText(this, "Error: ${exception.cause}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(mBinding.root)
        val mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            runBlocking {
                val checkLogin = mainActivityViewModel.onClickedLogin(
                        binding.etFirstName.text.toString(),
                        binding.etSecondName.text.toString(),
                        binding.etIDNP.text.toString(),
                        handler
                )
                if (checkLogin != null){
                    val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                    intent.putExtra("user", checkLogin.hashCode())
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //go to database population screen
        binding.btnToDB.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabaseVaccinationActivity::class.java)
            startActivity(intent)
        }
        binding.btnToTestDb.setOnClickListener{
            val intent = Intent(this@MainActivity, DatabaseTestActivity::class.java)
            startActivity(intent)
        }

        binding.btnToPeople.setOnClickListener {
            val intent = Intent(this@MainActivity, DatabasePersonActivity::class.java)
            startActivity(intent)
        }
    }
}