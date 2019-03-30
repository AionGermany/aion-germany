@echo off
start javaw -Xms1024m -Xmx1024m -cp ./libs/*;packetsamurai.jar com.aionemu.packetsamurai.PacketSamurai
exit