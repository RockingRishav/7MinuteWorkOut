package com.example.a7minutesworkout

class ExerciseViewModel (
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean,
    private var isSelected: Boolean
){
    fun getId() : Int{
        return id
    }
    fun setId(id : Int){
        this.id = id
    }
    fun getName() : String {
        return name
    }
    fun getImage() : Int{
        return image
    }
    fun setImage(image :Int){
        this.image =image
    }
    fun setIsSelected(po : Boolean){
        this.isSelected = po
    }
    fun setIsCompleted(po : Boolean){
        this.isCompleted = po
    }
    fun getIsCompleted(): Boolean{
        return  isCompleted
    }
    fun getIsSelected(): Boolean{
        return  isSelected
    }
}