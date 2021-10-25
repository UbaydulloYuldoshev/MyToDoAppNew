package uz.gita.mytodoappwithdagger.model

data class CheckData(
    val id : Int,
    var text:String,
    var isChecked : Int  // 0 false, 1 true
)

