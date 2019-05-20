package ac.id.pnj.broadbandmultimedia.bm2015

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick
//import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        button_calculator.text = resources.getText(R.string.button_calculator)
        button_history.text = resources.getText(R.string.button_history)
        button_reference.text = resources.getText(R.string.button_reference)

        Glide.with(applicationContext).load(R.drawable.logo_kelas).into(logo_bm)

        button_calculator.onClick {
            startActivity(Intent(this@DashboardActivity,MainActivity::class.java))
        }
        button_history.onClick {
            startActivity(Intent(this@DashboardActivity,SavedActivity::class.java))
        }
        button_reference.onClick {
            //
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean  = when (item?.itemId){
        R.id.menu_language -> {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
