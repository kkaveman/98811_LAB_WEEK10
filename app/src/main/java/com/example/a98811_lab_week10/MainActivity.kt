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


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    // ❌ Remove this - don't need it anymore
    // private var total: Int = 0

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

        // ✅ Call this to use ViewModel
        prepareViewModel()
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            updateText(total)
        }
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // ❌ Remove this - not needed anymore
    // private fun incrementTotal() {
    //     total++
    //     updateText(total)
    // }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }
}