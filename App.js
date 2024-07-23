import React, { Component } from 'react';
import { View, Button, NativeModules, NativeEventEmitter } from 'react-native';

const { MyIdModule } = NativeModules;

const myidEvents = new NativeEventEmitter(MyIdModule);

export default class App extends Component {

  constructor() {
    super();

    this.startSdk  = this.startSdk.bind(this);
  }

  UNSAFE_componentWillMount() {
    myidEvents.addListener('onSuccess', result =>
        Alert.alert(
          'onSuccess received',
        )
    );
    myidEvents.addListener('onError', result =>
        Alert.alert(
          'onError received',
        )
    );
    myidEvents.addListener('onUserExited', result =>
        Alert.alert(
          'onUserExited received',
        )
    );
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
        }}>
      <Button
        title="Start MyID"
        color="#841584"
        onPress={ this.startSdk }
      />
    </View>
    );
  }

  startSdk() {
    MyIdModule.startMyId();
  }
}