import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:get/get.dart';

void main() async {
  String value = "EFJHcHplbkp1WTNOWVFXOVIgUAGCjCWo8VkhC8kdpmEljVB1aLzZB3H91YNFuQ2D9hgsg6ezdkXMEE0ld9G5wj/E";
  String key = 'DjszrncsXAoQ2bed';

  const platform = MethodChannel('samples.flutter.dev/battery');
  final String greeting =
      await platform.invokeMethod('decrypt', {'value': value, 'key': key});
  print(greeting);

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Secure App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: Container(
        child: Image.asset('assets/image.png'),
      ),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String encryptedData = '';
  String decryptedData = '';

  static const encryptionChannel = const MethodChannel('enc/dec');

  Future<void> encryptData(String encrypted, String key) async {
    try {
      var result = await encryptionChannel.invokeMethod(
        'encrypt',
        {
          'data': "jsonString",
          'key': key,
        },
      );
      print('RETURNED FROM PLATFORM');
      print(result);
      setState(() {
        encryptedData = result;
      });
    } on PlatformException catch (e) {
      print('${e.message}');
    }
  }

  Future<void> decryptData(String encrypted, String key) async {
    try {
      var result = await encryptionChannel.invokeMethod('decrypt', {
        'data': encrypted,
        'key': key,
      });
      print('RETURNED FROM PLATFORM');
      print(result);
      setState(() {
        decryptedData = result;
      });
    } on PlatformException catch (e) {
      print('${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            MaterialButton(
              child: Text('Encrypt Data'),
              onPressed: () {
                encryptData('data to be encrypted', '16 character long key');
              },
            ),
            MaterialButton(
              child: Text('Decrypt Data'),
              onPressed: () {
                decryptData('data to be decrypted', '16 character long key');
                // same key used to encrypt
              },
            ),
            Text(encryptedData),
            Text(decryptedData),
          ],
        ),
      ),
    );
  }
}
