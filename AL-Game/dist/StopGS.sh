#!/bin/bash
if [ -e gameserver.pid ]
then
  gspid=`cat gameserver.pid`
  kill ${gspid}
  echo "GameServer stop signal sent"
else
  echo "GameServer is not running."
fi
exit 0