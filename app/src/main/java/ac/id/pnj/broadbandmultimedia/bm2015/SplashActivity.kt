package ac.id.pnj.broadbandmultimedia.bm2015

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Glide.with(applicationContext).load(R.drawable.logopnj).into(logo_pnj)
        tv_welcome_message.text = resources.getText(R.string.welcome_message)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        },4000)
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }


}
