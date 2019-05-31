package ac.id.pnj.broadbandmultimedia.bm2015

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reference_about.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ReferenceAboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reference_about)

        text_how_to.text = resources.getString(R.string.paragraph_how_to_use)

        referensi_dirjen.onClick {
            val refFiber = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.fiber_reference)))
            startActivity(refFiber)
        }

        referensi_corning_fiber.onClick {
            val refLossFiber = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.lossfiber_reference)))
            startActivity(refLossFiber)
        }

        referensi_connector.onClick {
            val refConn = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.connector_reference)))
            startActivity(refConn)
        }

        referensi_splicing.onClick {
            val refSplice = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.splices_reference)))
            startActivity(refSplice)
        }
    }
}
