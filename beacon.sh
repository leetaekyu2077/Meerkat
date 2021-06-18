#!/bin/bash
# ref: http://www.theregister.co.uk/Print/2013/11/29/feature_diy_apple_ibeacons/
set -x
# inquiry local bluetooth device
#hcitool dev
export BLUETOOTH_DEVICE=hci0
#sudo hcitool -i hcix cmd <OGF> <OCF> <No. Significant Data Octets> <iBeacon Prefix> <UUID> <Major> <Minor> <Tx Power> <Placeholder Octets>

#OGF = Operation Group Field = Bluetooth Command Group = 0x08
#OCF = Operation Command Field = HCI_LE_Set_Advertising_Data = 0x0008
#No. Significant Data Octets (Max of 31) = 1E (Decimal 30)
#iBeacon Prefix (Always Fixed) = 02 01 1A 1A FF 4C 00 02 15

export OGF="0x08"
export OCF="0x0008"
export IBEACONPROFIX="1E 02 01 1A 1A FF 4C 00 02 15"

#uuidgen  could gerenate uuid
export UUID="17 8D 75 63 48 BD 41 7C A3 B2 F6 F9 4D 77 B3 3D"
export MAJOR="00 00"
export MINOR="00 00"
export POWER="C8 00"


# initialize device
sudo hciconfig $BLUETOOTH_DEVICE up
# disable advertising
sudo hciconfig $BLUETOOTH_DEVICE noleadv
# stop the dongle looking for other Bluetooth devices
sudo hciconfig $BLUETOOTH_DEVICE noscan

# advertise
sudo hcitool -i $BLUETOOTH_DEVICE cmd 0x08 0x0008 $IBEACONPROFIX $UUID $MAJOR $MINOR $POWER
sudo hcitool -i $BLUETOOTH_DEVICE cmd 0x08 0x0006 A0 00 A0 00 00 00 00 00 00 00 00 00 00 07 00
sudo hcitool -i $BLUETOOTH_DEVICE cmd 0x08 0x000a 01

echo "complete" 
