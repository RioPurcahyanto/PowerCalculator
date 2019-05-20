package ac.id.pnj.broadbandmultimedia.bm2015

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {
    val konfigurasiNetworkSavedState:String = ""
    val powerTransmitterSavedState:String=""
    val panjangFiberSavedState:String=""
    val totalSplicesSavedState:String=""
    val totalConnectorSavedState:String=""
    val randomIdGeneratedSavedState:String=""

    var konfigurasiNetwork: String = ""
    var powerTransmitter: String = ""
    var panjangFiber: String = ""
    var totalSplices: String = ""
    var totalConnector: String = ""
    var randomIdGenerated: String= ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val randomId = (1..1000).random()
        val spinnerItems = resources.getStringArray(R.array.list_configuration)
        val spinnerAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner_konfig.adapter = spinnerAdapter

        spinner_konfig.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {

                konfigurasiNetwork = spinner_konfig.selectedItem.toString()
                when(konfigurasiNetwork){
                    "1:4:8" -> tv_power_splitter.text = "1 to 4 dan 1 to 8"
                    "1:4" -> tv_power_splitter.text = "1 to 4"
                    "1:2" -> tv_power_splitter.text = "1 to 2"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner_konfig.prompt = resources.getString(R.string.configurationChoice)
            }
        }
        btn_hitung.onClick {
            powerTransmitter = edit_powerTransmitter.text.toString()
            panjangFiber =  edit_panjang.text.toString()
            totalSplices = edit_splicing.text.toString()
            totalConnector = edit_connector.text.toString()
            randomIdGenerated =randomId.toString()

            if (powerTransmitter.isEmpty()){
                toast(resources.getString(R.string.checkPowerTransmitter))
            }
            if (panjangFiber.isEmpty()){
                toast(resources.getString(R.string.checkFiberLength))
            }
            if (totalSplices.isEmpty()){
                toast(resources.getString(R.string.checkTotalSplices))
            }
            if (totalConnector.isEmpty()){
                toast(resources.getString(R.string.checkTotalConnector))
            }
            if (konfigurasiNetwork.isBlank()){
                toast(resources.getString(R.string.checkNetworkConfig))
            }

            if (powerTransmitter.isNotEmpty() && panjangFiber.isNotEmpty() && totalConnector.isNotEmpty() &&
                    totalSplices.isNotEmpty() && konfigurasiNetwork.isNotEmpty()){
                val sendData = Intent(this@MainActivity,ResultActivity::class.java)
                sendData.putExtra("randomIdIntent",randomIdGenerated)
                sendData.putExtra("networkConfigurationIntent",konfigurasiNetwork)
                sendData.putExtra("powerTransmitterIntent",powerTransmitter)
                sendData.putExtra("panjangFiberIntent",panjangFiber)
                sendData.putExtra("totalSplicesIntent", totalSplices)
                sendData.putExtra("totalConnectorIntent", totalConnector)
                startActivity(sendData)
            }

        }
    }
}