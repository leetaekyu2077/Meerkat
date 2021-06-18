import serial
import time
from firebase import firebase
from pyfcm import FCMNotification
from time import sleep
import datetime

if __name__ == '__main__':
	#파이어베이스 연결
	firebase = firebase.FirebaseApplication("https://meerkatfb-1cf49-default-rtdb.firebaseio.com/", None)
	#FCM 서비스 연결
	APIKEY = "AAAA-P1-STw:APA91bHufehW4N_pKG0fTkwhS2GJWWkpkGqwVBHN_JZJLn2Mf4p9nwqwrKyhvFnCz-oqko0wOpRII_FEga-1ha-1YHKAr9lOx6G5kvSq8oVj8lrLfW4VRFNemGqtckg-x5frj9tj_LCE"
	push_service = FCMNotification(APIKEY)
	#테스트용 기기 토큰
	mToken = ""
	tokens = None
	result = None
	#허브아두이노 - 라즈베리파이 시리얼 통신 설정
	ser = serial.Serial('/dev/ttyACM0', 9600, timeout=1)
	ser.flush()

	while True:
		if ser.in_waiting > 0:
			line = ser.readline().decode('utf-8').rstrip()
			index = line.find('+')
			if index != -1:
				location = line[:index]
				value = int(line[index+1:])
				if value < 150:
					#print('Fire at ')
					#print(location)
					result = firebase.get('/token','')
					#print(line)
					registration_id = result
					time = str(datetime.datetime.now())
					data_message = {
						"title": "Fire!!",
						"body": "From " + location + " at " + time
					}
					print(time)
					result = push_service.single_device_data_message(registration_id=registration_id, data_message=data_message)
					print(result, "\n")
					sleep(10)
