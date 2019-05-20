package ac.id.pnj.broadbandmultimedia.bm2015.database

data class SavedResult(val id: Long?, val randomId:String, val configGpon:String?, val powerTransmitter: String?, val fiberlength:String?, val numberOfConnector:String?,
                       val numberOfSplicing:String?, val photoUri:String?){
    companion object {
        const val TABLE_SAVED: String = "TABLE_SAVED"
        const val ID: String = "ID_"
        const val RANDOM_ID:String = "RANDOM_ID"
        const val CONFIGURATION_GPON: String = "CONFIGURATION_GPON"
        const val POWER_TRANSMITTER: String = "POWER_TRANSMITTER"
        const val FIBER_LENGTH: String = "FIBER_LENGTH"
        const val CONNECTOR_NUMBER: String = "CONNECTOR_NUMBER"
        const val SPLICING_NUMBER:String = "SPLICING_NUMBER"
        const val PHOTO_URI:String = "PHOTO_URI"
    }
}