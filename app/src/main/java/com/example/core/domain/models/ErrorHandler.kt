package com.example.core.domain.models

enum class Errors {
    PAGE_NOT_FOUND,
    UNKNOWN,
    NO_INTERNET_CONNECTION

}

object ErrorHandler {


    private fun getError(errorCode: Int): Errors {
        if (errorCode == 404) {
            Errors.PAGE_NOT_FOUND
        }
        return Errors.UNKNOWN
    }

   private fun getErrorMessage(error: Errors):String{
       if (error == Errors.PAGE_NOT_FOUND){
           return "Page not found"
       }
       return "Something went wrong"
   }

}