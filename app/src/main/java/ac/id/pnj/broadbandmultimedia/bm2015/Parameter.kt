package ac.id.pnj.broadbandmultimedia.bm2015

class Parameter {
    var numberId: String? = null
    var parameter: String? = null
    var parValue: String? = null


    constructor(numberId: String?, parameter:String?,parValue:String?){
        this.numberId = numberId
        this.parameter = parameter
        this.parValue= parValue
    }

    override fun toString(): String {
        return "Parameter [numberId ="+numberId+", parameter="+parameter+", parValue="+parValue+"]"
    }

}