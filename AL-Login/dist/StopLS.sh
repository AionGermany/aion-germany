#!/bin/sh
if [ -e loginserver.pid ]
then
  lspid=`cat loginserver.pid`
  kill ${lspid}
  echo "LoginServer stop signal sent."
else
  echo "LoginServer is not running."
fi