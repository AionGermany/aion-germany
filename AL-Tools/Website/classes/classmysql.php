<?php
   /*--------------------------------------------------------------------------*/
   /* Modul...: classmysql.php    Klasse: ClassMysql                           */
   /* Erstellt: AION EMU EU, Mariella, 01/2016                                 */
   /* Zweck...: beinhaltet alle notwendigen Funktionen für die Zugriffe auf    */
   /*           Tabellen im MySql-Umfeld und vereinfacht damit die Handhabung  */
   /*--------------------------------------------------------------------------*/
   class ClassMysql 
   {
      var $link_id;
      var $query_result;
      var $num_queries = 0;
      var $aktDbName = "";

      function ClassMysql()
      {
      }
      // -------------------------------------------------------------------------
      // Connect to Mysql server
      // Verbindung zum MySql-Server aufbauen
      // -------------------------------------------------------------------------
      function connect($db_host, $db_user, $db_pass, $db_name = '') 
      {
         $this->link_id = @mysql_connect($db_host, $db_user, $db_pass, true);

         if ($this->link_id)
         {
            if($db_name)
            {
               if (@mysql_select_db($db_name, $this->link_id)) 
			   {
                  $this->aktDbName = $db_name;
                  return $this->link_id;
			   }
               else 
                  die("ClassMySql: Error - use database $db_name not possible");
            }
         } 
         else 
            die("ClassMySql: Error - connection not possible<br>".@mysql_error());
      }
      // -------------------------------------------------------------------------
      // close connection
      // Verbindung schliessen
      // -------------------------------------------------------------------------
      function close()
      {
          global $tot_queries;
          
          $tot_queries += $this->num_queries;
          if ($this->link_id)
          {
               if ($this->query_result) 
                  @mysql_free_result($this->query_result);
                  
               return @mysql_close($this->link_id);
          } 
            else return false;
      }
      // -------------------------------------------------------------------------
      // select current database
      // Datenbank auswählen
      // -------------------------------------------------------------------------
      function select_db($db_name) 
      {
         if ($this->link_id)
         {
            if (@mysql_select_db($db_name, $this->link_id)) 
               return $this->link_id;
            else 
               die("ClassMysql: Error - use database $db_name not possible");
         } 
         else 
            die("ClassMysql: Error - connection not possible");
      }
      // -------------------------------------------------------------------------
      // query an sql command
      // SQL-Befehl ausführen
      // -------------------------------------------------------------------------
      function query($sql)
      {
         $this->query_result = @mysql_query($sql, $this->link_id);

         if ($this->query_result)
         {
            ++$this->num_queries;
            return $this->query_result;
         } 
         else 
         {
            die("ClassMysql-Error: ".mysql_error($this->link_id));
            return false;
         }
      }
      // -------------------------------------------------------------------------
      // get result
      // Ergebnis abrufen
      // -------------------------------------------------------------------------
      function result($query_id = 0, $row = 0, $field = NULL)
      {
         return ($query_id) ? @mysql_result($query_id, $row, $field) : false;
      }
      // -------------------------------------------------------------------------
      // fetch next row from result as ROW
      // eine Ergebniszeile abrufen: ROW
      // -------------------------------------------------------------------------
      function fetch_row($query_id = 0)
      {
         return ($query_id) ? @mysql_fetch_row($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // fetch next row from result as ARRAY
      // eine Ergebniszeile abrufen: ARRAY
      // -------------------------------------------------------------------------
      function fetch_array($query_id = 0)
      {
         return ($query_id) ? @mysql_fetch_array($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // fetch next row from result as ASSOC
      // eine Ergebniszeile abrufen: ASSOC
      // -------------------------------------------------------------------------
      function fetch_assoc($query_id = 0)
      {
         return ($query_id) ? @mysql_fetch_assoc($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // statistic: count rows in result
      // Statistik: Anzahl Result-Zeilen 
      // -------------------------------------------------------------------------
      function num_rows($query_id = 0)
      {
         return ($query_id) ? @mysql_num_rows($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // statistic: count fields in result
      // Statistik: Anzahl Result-Felder 
      // -------------------------------------------------------------------------
      function num_fields($query_id = 0)
      {
         return ($query_id) ? @mysql_num_fields($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // statistic: count of affected lines
      // Statistik: Anzahl manipulierter Zeilen
      // -------------------------------------------------------------------------
      function affected_rows()
      {
         return ($this->link_id) ? @mysql_affected_rows($this->link_id) : false;
      }
      // -------------------------------------------------------------------------
      // statistic: last id from autoincrement for field
      // Statistik: letzte Id für Autoincrement-Feld
      // -------------------------------------------------------------------------
      function insert_id()
      {
         return ($this->link_id) ? @mysql_insert_id($this->link_id) : false;
      }
      // -------------------------------------------------------------------------
      // statistic: count of parallel queries
      // Statistik: Anzahl paralleler Queries
      // -------------------------------------------------------------------------
      function get_num_queries()
      {
         return $this->num_queries;
      }
      // -------------------------------------------------------------------------
      // free result
      // Result freigeben
      // -------------------------------------------------------------------------
      function free_result($query_id = false)
      {
         return ($query_id) ? @mysql_free_result($query_id) : false;
      }
      // -------------------------------------------------------------------------
      // get fieldtype from result field
      // Feldtyp ermitteln
      // -------------------------------------------------------------------------
      function field_type($query_id = 0,$field_offset)
      {
         return ($query_id) ? @mysql_field_type($query_id,$field_offset) : false;
      }
      // -------------------------------------------------------------------------
      // get fieldname from result field
      // Feldname ermitteln
      // -------------------------------------------------------------------------
      function field_name($query_id = 0,$field_offset)
      {
         return ($query_id) ? @mysql_field_name($query_id,$field_offset) : false;
      }
      // -------------------------------------------------------------------------
      // get all fieldnames as array from result
      // Feldnamen als Array zurückgeben
      // -------------------------------------------------------------------------
      function fieldNamesArray($query_id=0)
      {
         $tab = array();
         
         for ($i=0;$i<$this->num_fields($query_id);$i++)
         {
            $tab[] = $this->field_name($query_id,$i);
         }
         
         return $tab;
      }
      // -------------------------------------------------------------------------
      // get last error message
      // Fehlermeldung von MySql ermitteln
      // -------------------------------------------------------------------------
      function error()
      {
         return @mysql_error($this->link_id);
      }
      // -------------------------------------------------------------------------
      // 
      //                  E X I S T E N Z - P R Ü F U N G E N
      //
      // -------------------------------------------------------------------------
      // exists database
      // Datenbank vorhanden
      // -------------------------------------------------------------------------
      function existsDatabase($chkDbName)
      {
          $bef = "SELECT COUNT(*) FROM information_schema.schemata WHERE ".
                 "schema_name = '$chkDbName' ";
          $anz = $this->result($this->query($bef),0);
          
          if ($anz > 0)
              return true;
          else
              return false;
      }
      // -------------------------------------------------------------------------
      // exists table
      // Tabelle vorhanden (in aktueller Datenbank)
      // -------------------------------------------------------------------------
      function existsTable($chkTableName, $chkDbName="")
      {
          if ($chkDbName == "") $chkDbName = $this->aktDbName;
          
          $bef = "SELECT COUNT(*) FROM information_schema.tables WHERE ".
                 "table_schema = '$chkDbName' AND ".
                 "table_name = '$chkTableName'";
          $anz = $this->result($this->query($bef),0);
          
          if ($anz > 0)
              return true;
          else
              return false;
      }
      // -------------------------------------------------------------------------
      // exists field in table
      // Feld in Tabelle vorhanden (in aktueller Datenbank)
      // -------------------------------------------------------------------------
      function existsField($chkTableName, $chkFieldName)
      {
          $bef = "SELECT COUNT(*) FROM information_schema.columns WHERE ".
                 "table_name = '$chkTableName' AND ".
                 "column_name = '$chkFieldName'";
          $anz = $this->result($this->query($bef),0);
          
          if ($anz > 0)
              return true;
          else
              return false;
      }
   }
?>
