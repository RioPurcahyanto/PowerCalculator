package ac.id.pnj.broadbandmultimedia.bm2015

import ac.id.pnj.broadbandmultimedia.bm2015.database.SavedResult
import ac.id.pnj.broadbandmultimedia.bm2015.database.database
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteConstraintException
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsv
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {
    private lateinit var photoView: ImageView
    private lateinit var buttonPhoto: Button
    private lateinit var buttonPrint: Button
    private var photoFile: File? = null
    private var CAPTURE_IMAGE_REQUEST = 1
    private lateinit var photoPath: String
    private val IMAGE_DIRECTORY_NAME = "SAVED_PHOTO"

    val CSV_HEADER = "No \tParameter \tValue"


    private lateinit var scrollView: ScrollView
    private lateinit var btnScreenshot: Button

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    var powerTransmitterReceived: String = ""
    var panjangFiberReceived: String = ""
    var totalSplicesReceived: String = ""
    var totalConnectorReceived: String = ""
    var networkConfigurationReceived: String = ""
    var randomIdReceived: String = ""
    var photoUriReceived: String? = ""
    var powerSplitterUsedReceived: String? = ""

    var powerTransmitterDouble: Double = 0.0
    var panjangFiberDouble: Double = 0.0
    var totalSplicesDouble: Double = 0.0
    var totalConnectorDouble: Double = 0.0
    var splitter1: Double = 0.0
    var splitter2: Double = 0.0

    var totalLossSplitter1: Double = 0.0
    var totalLossSplitter2: Double = 0.0

    val lossFiber = 0.21
    val lossSplices = 0.1
    val lossConnector = 0.75
    val lossSplitter1to2 = 4.2
    val lossSplitter1to4 = 7.6
    val lossSplitter1to8 = 11.0

    var totalLossFiber: Double = 0.0
    var totalLossConnector: Double = 0.0
    var totalLossSplicing: Double = 0.0
    var totalLossSplitter: Double = 0.0
    var totalLossPure: Double = 0.0
    var totalLossResult: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)



        //for taking a photo
        photoView = find(R.id.img_photo)
        buttonPhoto = find(R.id.btn_take_photo)
        buttonPhoto.onClick {
            captureImage()
        }

        //to get data from calculator
        val sendData = intent
        powerTransmitterReceived = sendData.getStringExtra("powerTransmitterIntent")
        panjangFiberReceived = sendData.getStringExtra("panjangFiberIntent")
        totalSplicesReceived = sendData.getStringExtra("totalSplicesIntent")
        totalConnectorReceived = sendData.getStringExtra("totalConnectorIntent")
        networkConfigurationReceived = sendData.getStringExtra("networkConfigurationIntent")
        randomIdReceived = sendData.getStringExtra("randomIdIntent")
        photoUriReceived = sendData.getStringExtra("photoUriIntent")
        powerSplitterUsedReceived = sendData.getStringExtra("powerSplitterUsedIntent")

        supportActionBar?.setTitle("ID Result : " + randomIdReceived)
        //to load image from database
        Glide.with(applicationContext).load(photoUriReceived).into(photoView)




        if (networkConfigurationReceived.equals("1:4:8")) {
            splitter1 = 1.0
            splitter2 = 1.0
            totalLossSplitter1 = splitter1.times(lossSplitter1to4)
            totalLossSplitter2 = splitter2.times(lossSplitter1to8)
        }
        if (networkConfigurationReceived.equals("1:4")) {
            splitter1 = 1.0
            splitter2 = 0.0
            totalLossSplitter1 = splitter1.times(lossSplitter1to4)
            totalLossSplitter2 = splitter2
        }
        if (networkConfigurationReceived.equals("1:2")) {
            splitter1 = 1.0
            splitter2 = 0.0
            totalLossSplitter1 = splitter1.times(lossSplitter1to2)
            totalLossSplitter2 = splitter2
        }

        panjangFiberDouble = panjangFiberReceived.toDouble()
        powerTransmitterDouble = powerTransmitterReceived.toDouble()
        totalConnectorDouble = totalConnectorReceived.toDouble()
        totalSplicesDouble = totalSplicesReceived.toDouble()

        totalLossFiber = panjangFiberDouble.times(lossFiber)
        totalLossConnector = totalConnectorDouble.times(lossConnector)
        totalLossSplicing = totalSplicesDouble.times(lossSplices)
        totalLossSplitter = totalLossSplitter1.plus(totalLossSplitter2)
        totalLossPure = totalLossConnector.plus(totalLossFiber).plus(totalLossSplitter).plus(totalLossSplicing)
        totalLossResult = powerTransmitterDouble.minus(totalLossPure)

        val printPowerTransmitter: String = powerTransmitterReceived + resources.getString(R.string.spasiDBM)
        val printLossFiber: String =
            panjangFiberReceived + resources.getString(R.string.spasiKMspasiXspasi) + lossFiber.toString() + resources.getString(
                R.string.spasiDBperKMnewLine
            ) + totalLossFiber.toString()
        val printLossConnector: String =
            totalConnectorReceived.toString() + resources.getString(R.string.spasiXspasi) + lossConnector.toString() + resources.getString(
                R.string.spasidBperpairnewLine
            ) + totalLossConnector.toString()
        val printLossSplices: String =
            totalSplicesReceived.toString() + resources.getString(R.string.spasiXspasi) + lossSplices.toString() + resources.getString(
                R.string.spasidbpersplicesnewline
            ) + totalLossSplicing.toString()
        val printLossSplitter: String =
            "Splitter 1 = " + totalLossSplitter1 + "\n" + "Splitter 2 = " + totalLossSplitter2 + "\n" + resources.getString(
                R.string.resultView
            ) + totalLossSplitter
        val printTotal: String = "= Power Transmitter - Total Loss \n" +
                "= Power Transmitter - (" + resources.getString(R.string.result_loss_fiber) + " - " + resources.getString(
            R.string.result_loss_connector
        ) + " - " + resources.getString(R.string.result_loss_splices) + " - Loss Splitter) \n" +
                "= " + powerTransmitterReceived.toString() + " - " + totalLossPure.toString() + "\n " +
                "= " + powerTransmitterReceived.toString() + " - (" + totalLossFiber.toString() + " - " + totalLossConnector.toString() + " - " + totalLossSplicing.toString() + " - " +
                totalLossSplitter.toString() + ")\n" +
                "\n" + resources.getString(R.string.resultView) + totalLossResult.toString() + " dB"
        tv_konfigurasi.text = networkConfigurationReceived
        tv_power_transmitter.text = printPowerTransmitter
        tv_loss_fiber_optik.text = printLossFiber
        tv_loss_connector.text = printLossConnector
        tv_loss_splices.text = printLossSplices
        tv_loss_power_splitter.text = printLossSplitter
        tv_total_loss.text = printTotal

        favoriteState()

        //for printing data
        buttonPrint = find(R.id.btn_print)
        buttonPrint.onClick {
            printFile()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.star_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_database -> {
                if (isFavorite) removeFromSaved() else addToSaved()
                isFavorite = !isFavorite
                setFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToSaved() {
        try {
            database.use {
                insert(
                    SavedResult.TABLE_SAVED,
                    SavedResult.RANDOM_ID to randomIdReceived,
                    SavedResult.CONFIGURATION_GPON to networkConfigurationReceived,
                    SavedResult.POWER_TRANSMITTER to powerTransmitterReceived,
                    SavedResult.FIBER_LENGTH to panjangFiberReceived,
                    SavedResult.CONNECTOR_NUMBER to totalConnectorReceived,
                    SavedResult.SPLICING_NUMBER to totalSplicesReceived,
                    SavedResult.PHOTO_URI to photoUriReceived,
                    SavedResult.POWER_SPLITTERUSED to powerSplitterUsedReceived
                )
            }
            tv_total_loss.snackbar("Saved").show()
        } catch (e: SQLiteConstraintException) {
            tv_total_loss.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromSaved() {
        try {
            database.use {
                delete(
                    SavedResult.TABLE_SAVED, "(RANDOM_ID = {randomIdReceived})",
                    "randomIdReceived" to randomIdReceived
                )
            }
            photoFile?.delete()
            tv_total_loss.snackbar("Removed from Saved").show()
        } catch (e: SQLiteConstraintException) {
            tv_total_loss.snackbar(e.localizedMessage).show()
        }

    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_database)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_database)
    }

    private fun favoriteState() {
        database.use {
            val result = select(SavedResult.TABLE_SAVED)
                .whereArgs(
                    "(RANDOM_ID = {randomIdReceived})",
                    "randomIdReceived" to randomIdReceived
                )
            val favorite = result.parseList(classParser<SavedResult>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    fun captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                try {
                    photoFile = createImageFile()
                    displayMessage(baseContext, photoFile!!.absolutePath)
                    Log.i("Test", photoFile!!.absolutePath)

                    if (photoFile != null) {
                        var photoURI = FileProvider.getUriForFile(
                            this,
                            "ac.id.pnj.broadbandmultimedia.bm2015.fileprovider", photoFile!!
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                } catch (ex: Exception) {
                    displayMessage(baseContext, "Capture Image Bug: " + ex.message.toString())
                }
            } else {
                displayMessage(baseContext, "Null")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            photoView.setImageBitmap(myBitmap)
            photoUriReceived = photoPath
        } else {
            displayMessage(baseContext, "Request cancelled or something went wrong.")
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        toast(message)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                captureImage()
            }
        }

    }


    fun printFile() {
        var fileWriter: FileWriter? = null
        val parameters = Arrays.asList(
            Parameter("1", "GPON Configuration", networkConfigurationReceived),
            Parameter("2", "Power Transmitter", powerTransmitterReceived),
            Parameter("3", "Fibre Optic Length", panjangFiberReceived),
            Parameter("4", "Connector", totalConnectorReceived),
            Parameter("5", "Splices", totalSplicesReceived),
            Parameter("6", "Power Splitter", powerSplitterUsedReceived),
            Parameter("7", "Optical Fiber Loss", totalLossFiber.toString()),
            Parameter("8", "Connector Loss", totalLossConnector.toString()),
            Parameter("9", "Splicing Loss", totalLossSplicing.toString()),
            Parameter("10", "Splitter Loss", totalLossSplitter.toString()),
            Parameter("11", "Total Loss", totalLossPure.toString()),
            Parameter("12", "Power Received", totalLossResult.toString())
        )
        try {
            val baseDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val filename = "GPON_ID-"+randomIdReceived+".csv"

            fileWriter = FileWriter(baseDir?.toString()+"/"+filename)

            fileWriter.append(CSV_HEADER)
            fileWriter.append('\n')

            for (parameter in parameters) {
                fileWriter.append(parameter.numberId)
                fileWriter.append(',')
                fileWriter.append(parameter.parameter)
                fileWriter.append(',')
                fileWriter.append(parameter.parValue)
                fileWriter.append('\n')
            }

            toast("Write CSV successfully!")

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            emailIntent.setType("*/*")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"GPON RESULT ID : "+randomIdReceived)
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                "1. GPON Configuration  : "+networkConfigurationReceived+"\n"+
                    "2. Power Transmitter  : "+powerTransmitterReceived +"\n" +
                    "3. Fibre Optic Length  : "+panjangFiberReceived +"\n" +
                    "4. Connector  : "+totalConnectorReceived +"\n" +
                    "5. Splices  : "+totalSplicesReceived +"\n" +
                    "6. Power Splitter  : "+powerSplitterUsedReceived +"\n" +
                    "7. Optical Fiber Loss  : "+totalLossFiber +"\n" +
                    "8. Connector Loss  : "+totalLossConnector +"\n" +
                    "9. Splicing Loss  : "+totalLossSplicing +"\n" +
                    "10. Splitter Loss  : "+totalLossSplitter +"\n" +
                    "11. Total Loss  : "+totalLossPure +"\n" +
                    "12. Power Received  : "+totalLossResult
            )
            val builder:StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val fileSend:File = File(baseDir?.toString()+"/"+filename)
            val uriSend:Uri = FileProvider.getUriForFile(applicationContext,"ac.id.pnj.broadbandmultimedia.bm2015.fileprovider",fileSend)
            emailIntent.putExtra(Intent.EXTRA_STREAM,uriSend)
            startActivity(Intent.createChooser(emailIntent,"Share The Result"))
        } catch (e: Exception) {
            toast("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }
}
