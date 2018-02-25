/*
   Mariella, 01/2016, added Language-Support
*/   
function setLang(lang)
{    
    document.cookie = "langwahl=" + lang;

    location.reload();
}