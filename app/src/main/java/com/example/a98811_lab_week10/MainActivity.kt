package com.example.a98811_lab_week10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a98811_lab_week10.viewmodels.TotalViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.a98811_lab_week10.database.*


class MainActivity : AppCompatActivity() {


    private val db by lazy { prepareDatabase() }
    // Create an instance of the TotalViewModel
// by lazy is used to create the ViewModel only when it's needed

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        initializeValueFromDatabase()
        // ✅ Call this to use ViewModel
        prepareViewModel()
    }

    override fun onPause() {
        super.onPause()
        db.totalDao().update(Total(ID, viewModel.total.value!!))
    }
    private fun prepareViewModel(){
        // Observe the LiveData object
        viewModel.total.observe(this, {
            // Whenever the value of the LiveData object changes
            // the updateText() is called, with the new value as the

            updateText(it)
        })
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }


        private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(Total(id = 1, total = 0))
        } else {
            viewModel.setTotal(total.first().total)
        }
    }

    companion object {
        const val ID: Long = 1
    }

}