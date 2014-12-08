package com.example.flightsofangels;

//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.Color;
//import android.net.NetworkInfo;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.net.wifi.WpsInfo;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pDeviceList;
//import android.net.wifi.p2p.WifiP2pGroup;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.net.wifi.p2p.WifiP2pManager.Channel;
//import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
//import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
//import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
//import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//public class WiFiDirectTestAppActivity extends Activity {
//
//	private final String TAG = "WiFiDirectTestAppActivity";
//
//	private TextView mTextView_Log;
//
//	private final String LINE_SEPARATOR = System.getProperty("line.separator");
//	private final String LINE_SEPARATOR_HTML = "<br />";
//
//	private boolean HTML_OUT = true;
//
//	private enum ReceiverState {
//		All, StateChange, PeersChange, ConnectionChange, ThisDeviceChange,
//	}
//
//	private BroadcastReceiver mReceiver;
//
//	private WDBR_P2P_STATE_CHANGED_ACTION mWDBR_P2P_STATE_CHANGED_ACTION;
//
//	private WDBR_P2P_PEERS_CHANGED_ACTION mWDBR_P2P_PEERS_CHANGED_ACTION;
//
//	private WDBR_P2P_CONNECTION_CHANGED_ACTION mWDBR_P2P_CONNECTION_CHANGED_ACTION;
//
//	private WDBR_P2P_THIS_DEVICE_CHANGED_ACTION mWDBR_THIS_DEVICE_CHANGED_ACTION;
//
//	private boolean mIsWiFiDirectEnabled;
//
//	private WifiP2pManager mWifiP2pManager;
//
//	private Channel mChannel;
//
//	private List<WifiP2pDevice> mPeers = new ArrayList<WifiP2pDevice>();
//
//	private ActionListenerAdapter mActionListenerAdapter;
//
//	private Spinner mPeersSpinner;
//
//	private String mSelectedDevice;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.main);
//
//		String title = "ANDROID_ID[" + getAndroid_ID() + "]";
//		title += "   MAC[" + getMACAddress() + "]";
//		setTitle(title);
//
//		initializeLog();
//		initBroadcastToggle();
//
//		addLog("onCreate()");
//
//		if (!hasP2P()) {
//			toastAndLog("onCreate()", "This Device Has Not P2P Feature!!");
//		}
//		// new FileServerAsyncTask().execute();
//		// final Button loadButton=(Button) findViewById(R.id.button1);
//		// loadButton.setOnClickListener(new OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View v) {
//		// new HttpGetTask().execute();
//		//
//		// }
//		// });
//	}
//
//	public void execAsync(View view) {
//		// AppContext context = new AppContext();
//
//		FileServerAsyncTask obj = new FileServerAsyncTask();
//		addLog("Calling Async execute");
//		obj.execute();
//	}
//
//	class FileClientAsyncTask extends AsyncTask<Void, Void, Void> {
//		String destAddress;
//		int destPort;
//		String response;
//
//		FileClientAsyncTask(String destAddress, int destPort) {
//			this.destAddress = destAddress;
//			this.destPort = destPort;
//
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			Socket socket = null;
//			try {
//				socket = new Socket(destAddress, destPort);
//				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
//						1024);
//				byte buffer[] = new byte[1024];
//				InputStream inputStream = socket.getInputStream();
//				int bytesRead=-1;
//				do {
//					bytesRead = inputStream.read(buffer);
//					byteArrayOutputStream.write(buffer, 0, bytesRead);
//					response+=byteArrayOutputStream.toString("UTF-8");
//				} while (bytesRead != -1);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				WiFiDirectTestAppActivity.this.addLog(e.toString());
//				response="Exception " + e.toString();
//
//			}
//			return null;
//		}
//		
//		protected void onPostExecute(Void result)
//		{
//			WiFiDirectTestAppActivity.this.addLog("In onPostExecute()");
//			super.onPostExecute(result);
//		}
//
//	}
//
//	class FileServerAsyncTask extends AsyncTask<Void, String, String> {
//		private Context context;
//		private TextView statusText;
//
//		// Add a constructor
//		// FileServerAsyncTask(Context context, View statusText) {
//		// this.context = context;
//		// this.statusText = (TextView) statusText;
//		// }
//
//		protected void onPreExecute() {
//			super.onPreExecute();
//			publishProgress("Going to execute now. At onPreExecute");
//		}
//
//		@Override
//		protected String doInBackground(Void... params) {
//			publishProgress("I'm the background task");
//
//			// ServerSocket serverSocket;
//			try {
//				publishProgress("I'm the background task inside the try block.");
//				ServerSocket serverSocket = new ServerSocket(8888);
//				publishProgress("Just create the socket!");
//				Socket client = serverSocket.accept();
//				publishProgress("Socket created");
//				InputStream inputStream = client.getInputStream();
//				String data = "Change this later on.";
//				int size = 1024;
//				byte arr[] = new byte[1024];
//				arr = data.getBytes();
//				OutputStream outputStream = client.getOutputStream();
//				publishProgress("Stream ready to write");
//				outputStream.write(arr, 0, arr.length);
//				publishProgress("Written from my side. Baaki doosre ka dekh lo");
//				return data;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				publishProgress(e.toString());
//
//				publishProgress("Error in socket formation");
//				return null;
//			}
//
//		}
//
//		protected void onProgressUpdate(String... params) {
//			WiFiDirectTestAppActivity.this.addLog(params[0]);
//		}
//
//		protected void onPostExecute(String result) {
//			if (result != null) {
//				WiFiDirectTestAppActivity.this
//						.addLog("Beeg American Teetees!!!");
//				WiFiDirectTestAppActivity.this
//						.addLog("String sent successfully!!");
//				// Intent intent = new Intent();
//				// intent.setAction(android.content.Intent.ACTION_VIEW);
//				// context.startActivity(intent);
//			} else
//				;
//			// publishProgress("Not Transfered");
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		addLog("onResume()");
//
//		mIsWiFiDirectEnabled = false;
//
//		registerBroadcastReceiver(ReceiverState.All);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		addLog("onPause()");
//
//		unRegisterBroadcastReceiver(ReceiverState.All);
//	}
//
//	private void registerBroadcastReceiver(ReceiverState rs) {
//		IntentFilter filter = new IntentFilter();
//
//		switch (rs) {
//		case All:
//			filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//			filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//			filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//			filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//			mReceiver = new WiFiDirectBroadcastReceiver();
//			registerReceiver(mReceiver, filter);
//			addLog("registerBroadcastReceiver() BroadcastReceiver");
//			break;
//
//		case StateChange:
//			filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//			mWDBR_P2P_STATE_CHANGED_ACTION = new WDBR_P2P_STATE_CHANGED_ACTION();
//			registerReceiver(mWDBR_P2P_STATE_CHANGED_ACTION, filter);
//			addLog("registerBroadcastReceiver() P2P_STATE_CHANGED_ACTION");
//			break;
//
//		case PeersChange:
//			filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//			mWDBR_P2P_PEERS_CHANGED_ACTION = new WDBR_P2P_PEERS_CHANGED_ACTION();
//			registerReceiver(mWDBR_P2P_PEERS_CHANGED_ACTION, filter);
//			addLog("registerBroadcastReceiver() P2P_PEERS_CHANGED_ACTION");
//			break;
//
//		case ConnectionChange:
//			filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//			mWDBR_P2P_CONNECTION_CHANGED_ACTION = new WDBR_P2P_CONNECTION_CHANGED_ACTION();
//			registerReceiver(mWDBR_P2P_CONNECTION_CHANGED_ACTION, filter);
//			addLog("registerBroadcastReceiver() P2P_CONNECTION_CHANGED_ACTION");
//			break;
//
//		case ThisDeviceChange:
//			filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//			mWDBR_THIS_DEVICE_CHANGED_ACTION = new WDBR_P2P_THIS_DEVICE_CHANGED_ACTION();
//			registerReceiver(mWDBR_THIS_DEVICE_CHANGED_ACTION, filter);
//			addLog("registerBroadcastReceiver() THIS_DEVICE_CHANGED_ACTION");
//			break;
//
//		default:
//			toastAndLog("registerBroadcastReceiver()", "Unknown ReceiverState["
//					+ rs + "]");
//			break;
//		}
//	}
//
//	private void unRegisterBroadcastReceiver(ReceiverState rs) {
//		switch (rs) {
//		case All:
//			if (mReceiver != null) {
//				unregisterReceiver(mReceiver);
//				mReceiver = null;
//				addLog("unRegisterBroadcastReceiver() BroadcastReceiver");
//			}
//			break;
//
//		case StateChange:
//			if (mWDBR_P2P_STATE_CHANGED_ACTION != null) {
//				unregisterReceiver(mWDBR_P2P_STATE_CHANGED_ACTION);
//				mWDBR_P2P_STATE_CHANGED_ACTION = null;
//				addLog("unRegisterBroadcastReceiver() P2P_STATE_CHANGED_ACTION");
//			}
//			break;
//
//		case PeersChange:
//			if (mWDBR_P2P_PEERS_CHANGED_ACTION != null) {
//				unregisterReceiver(mWDBR_P2P_PEERS_CHANGED_ACTION);
//				mWDBR_P2P_PEERS_CHANGED_ACTION = null;
//				addLog("unRegisterBroadcastReceiver() P2P_PEERS_CHANGED_ACTION");
//			}
//			break;
//
//		case ConnectionChange:
//			if (mWDBR_P2P_CONNECTION_CHANGED_ACTION != null) {
//				unregisterReceiver(mWDBR_P2P_CONNECTION_CHANGED_ACTION);
//				mWDBR_P2P_CONNECTION_CHANGED_ACTION = null;
//				addLog("unRegisterBroadcastReceiver() P2P_CONNECTION_CHANGED_ACTION");
//			}
//			break;
//
//		case ThisDeviceChange:
//			if (mWDBR_THIS_DEVICE_CHANGED_ACTION != null) {
//				unregisterReceiver(mWDBR_THIS_DEVICE_CHANGED_ACTION);
//				mWDBR_THIS_DEVICE_CHANGED_ACTION = null;
//				addLog("unRegisterBroadcastReceiver() THIS_DEVICE_CHANGED_ACTION");
//			}
//			break;
//
//		default:
//			toastAndLog("unRegisterBroadcastReceiver()",
//					"Unknown ReceiverState[" + rs + "]");
//			break;
//		}
//	}
//
//	private void initBroadcastToggle() {
//		initBroadcastToggleInner(R.id.toggle_bc_all);
//		initBroadcastToggleInner(R.id.toggle_bc_state);
//		initBroadcastToggleInner(R.id.toggle_bc_peers);
//		initBroadcastToggleInner(R.id.toggle_bc_connection);
//		initBroadcastToggleInner(R.id.toggle_bc_this);
//	}
//
//	private void initBroadcastToggleInner(final int rId_Toggle) {
//
//		ToggleButton tb = (ToggleButton) findViewById(rId_Toggle);
//		tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				ReceiverState rs = ReceiverState.All;
//				switch (rId_Toggle) {
//				case R.id.toggle_bc_all:
//					rs = ReceiverState.All;
//					break;
//				case R.id.toggle_bc_state:
//					rs = ReceiverState.StateChange;
//					break;
//				case R.id.toggle_bc_peers:
//					rs = ReceiverState.PeersChange;
//					break;
//				case R.id.toggle_bc_connection:
//					rs = ReceiverState.ConnectionChange;
//					break;
//				case R.id.toggle_bc_this:
//					rs = ReceiverState.ThisDeviceChange;
//					break;
//				default:
//					toastAndLog("initBroadcastToggleInner()",
//							"Unknown ReceiverState[" + rs + "]");
//					return;
//				}
//
//				if (isChecked) {
//					registerBroadcastReceiver(rs);
//				} else {
//					unRegisterBroadcastReceiver(rs);
//				}
//			}
//		});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		menu.add(Menu.NONE, 0, Menu.NONE, getString(R.string.app_about));
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		boolean ret = true;
//		int id = item.getItemId();
//		switch (id) {
//		default:
//			toastAndLog("onOptionsItemSelected()", "Unknown Item Id[" + id
//					+ "]");
//			break;
//		case 0:
//			Toast.makeText(this, getAppVersion(), Toast.LENGTH_SHORT).show();
//			break;
//		}
//		return ret;
//	}
//
//	private void initializeLog() {
//		if (mTextView_Log != null) {
//			return;
//		}
//
//		mTextView_Log = (TextView) findViewById(R.id.textView_log);
//
//		mTextView_Log.addTextChangedListener(new TextWatcher() {
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//
//				ToggleButton tb = (ToggleButton) findViewById(R.id.toggle_autoscroll);
//				if (!tb.isChecked()) {
//					return;
//				}
//
//				final ScrollView sv = (ScrollView) findViewById(R.id.scrollview_log);
//				sv.post(new Runnable() {
//					public void run() {
//						sv.fullScroll(View.FOCUS_DOWN);
//					}
//				});
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			public void afterTextChanged(Editable s) {
//			}
//		});
//
//		RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup_logkind);
//		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.radiobutton_html:
//					HTML_OUT = true;
//					break;
//				case R.id.radiobutton_mono:
//					HTML_OUT = false;
//					break;
//				default:
//					addLog("initializeLog() Unknown Log Kind[" + checkedId
//							+ "]");
//					HTML_OUT = false;
//					break;
//				}
//			}
//		});
//	}
//
//	private void addLog(String log) {
//		Log.d(TAG, log);
//
//		log = log + nl();
//		if (mTextView_Log == null) {
//			initializeLog();
//		}
//		mTextView_Log.append(HTML_OUT ? convHtmlStr2CS(log) : log);
//	}
//
//	private CharSequence convHtmlStr2CS(String htmlStr) {
//		return Html.fromHtml(htmlStr);
//	}
//
//	private String nl() {
//		return HTML_OUT ? LINE_SEPARATOR_HTML : LINE_SEPARATOR;
//	}
//
//	private void addMethodLog(String method) {
//		if (HTML_OUT)
//			method = "<font color=lime>" + method + "</font>";
//		addLog(nl() + method);
//	}
//
//	private void toastAndLog(String msg1, String msg2) {
//		String log = msg1 + LINE_SEPARATOR + msg2;
//		Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
//
//		if (HTML_OUT)
//			log = "<font color=red>" + msg1 + nl() + msg2 + "</font>";
//		addLog(log);
//	}
//
//	public void onClickResetLog(View view) {
//		mTextView_Log.setText("");
//	}
//
//	public void onClickSaveLog(View view) {
//		String log = mTextView_Log.getText().toString();
//		Log.d(TAG, "onClickSaveLog() LOG[" + log + "]");
//	}
//
//	private String toStringDevice(WifiP2pDevice device) {
//		String log = separateCSV(device.toString()) + nl() + "　"
//				+ getDeviceStatus(device.status);
//		return HTML_OUT ? "<font color=yellow>" + log + "</font>" : log;
//	}
//
//	private String separateCSV(String csvStr) {
//
//		return csvStr.replaceAll("[^:yWFD] ", nl() + "　");
//
//	}
//
//	private String getDeviceStatus(int deviceStatus) {
//		String status = "";
//		switch (deviceStatus) {
//		case WifiP2pDevice.AVAILABLE:
//			status = "Available";
//			break;
//		case WifiP2pDevice.INVITED:
//			status = "Invited";
//			break;
//		case WifiP2pDevice.CONNECTED:
//			status = "Connected";
//			break;
//		case WifiP2pDevice.FAILED:
//			status = "Failed";
//			break;
//		case WifiP2pDevice.UNAVAILABLE:
//			status = "Unavailable";
//			break;
//		default:
//			status = "Unknown";
//			break;
//		}
//		return HTML_OUT ? "[<b><i><u>" + status + "</u></i></b>]" : "["
//				+ status + "]";
//	}
//
//	class ActionListenerAdapter implements WifiP2pManager.ActionListener {
//
//		public void onSuccess() {
//			String log = " onSuccess()";
//			if (HTML_OUT)
//				log = "<font color=aqua>　" + log + "</font>";
//			addLog(log);
//		}
//
//		public void onFailure(int reason) {
//			String log = " onFailure(" + getReason(reason) + ")";
//			if (HTML_OUT)
//				log = "<font color=red>　" + log + "</font>";
//			addLog(log);
//		}
//
//		private String getReason(int reason) {
//			String[] strs = { "ERROR", "P2P_UNSUPPORTED", "BUSY" };
//			try {
//				return strs[reason] + "(" + reason + ")";
//			} catch (ArrayIndexOutOfBoundsException e) {
//				return "UNKNOWN REASON CODE(" + reason + ")";
//			}
//		}
//	}
//
//	private boolean isNull(boolean both) {
//		if (mActionListenerAdapter == null) {
//			mActionListenerAdapter = new ActionListenerAdapter();
//		}
//
//		if (!mIsWiFiDirectEnabled) {
//			toastAndLog(" Wi-Fi Direct is OFF!", "try Setting Menu");
//			return true;
//		}
//
//		if (mWifiP2pManager == null) {
//			toastAndLog(" mWifiP2pManager is NULL!", " try getSystemService");
//			return true;
//		}
//		if (both && (mChannel == null)) {
//			toastAndLog(" mChannel is NULL!", " try initialize");
//			return true;
//		}
//
//		return false;
//	}
//
//	public void onClickGetSystemService(View view) {
//		addMethodLog("getSystemService(Context.WIFI_P2P_SERVICE)");
//
//		mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//
//		addLog("　Result[" + (mWifiP2pManager != null) + "]");
//	}
//
//	@SuppressLint("NewApi")
//	public void onClickInitialize(View view) {
//		addMethodLog("mWifiP2pManager.initialize()");
//		if (isNull(false)) {
//			return;
//		}
//
//		mChannel = mWifiP2pManager.initialize(this, getMainLooper(),
//				new ChannelListener() {
//					public void onChannelDisconnected() {
//						addLog("mWifiP2pManager.initialize() -> onChannelDisconnected()");
//					}
//				});
//
//		addLog("　Result[" + (mChannel != null) + "]");
//	}
//
//	public void onClickDiscoverPeers(View view) {
//		addMethodLog("mWifiP2pManager.discoverPeers()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.discoverPeers(mChannel, mActionListenerAdapter);
//	}
//
//	public void onClickConnect(View view) {
//		addMethodLog("mWifiP2pManager.connect()");
//		if (isNull(true)) {
//			return;
//		}
//
//		int cnt = mPeers.size();
//		if (cnt == 0) {
//			addLog(" peer not found! try discoverPeers & requestPeers");
//			return;
//		}
//
//		int idx = 0;
//		for (WifiP2pDevice device : mPeers) {
//			if (device.deviceName.equals(mSelectedDevice)) {
//				break;
//			}
//			idx += 1;
//		}
//
//		WifiP2pConfig config = new WifiP2pConfig();
//		config.deviceAddress = mPeers.get(idx).deviceAddress;
//
//		config.groupOwnerIntent = getOwnerIntentValue();
//
//		config.wps.setup = getWPSSetupValue();
//
//		addLog(" connecting to [" + mSelectedDevice + "]["
//				+ config.deviceAddress + "] G.O.[" + config.groupOwnerIntent
//				+ "] WPS[" + config.wps.setup + "]");
//
//		mWifiP2pManager.connect(mChannel, config, mActionListenerAdapter);
//	}
//
//	private int getOwnerIntentValue() {
//		Spinner sp = (Spinner) findViewById(R.id.spinner_go);
//		int v = sp.getSelectedItemPosition();
//		return v - 1;
//	}
//
//	private int getWPSSetupValue() {
//		Spinner sp = (Spinner) findViewById(R.id.spinner_wps);
//		int v = sp.getSelectedItemPosition();
//		switch (v) {
//		case 0:
//			return WpsInfo.PBC;
//		case 1:
//			return WpsInfo.DISPLAY;
//		case 2:
//			return WpsInfo.KEYPAD;
//		case 3:
//			return WpsInfo.LABEL;
//		case 4:
//			return WpsInfo.INVALID;
//		default:
//			toastAndLog("getWPSSetupValue()", "Unknown WPS Index[" + v + "]");
//			return WpsInfo.INVALID;
//		}
//	}
//
//	public void onClickCancelConnect(View view) {
//		addMethodLog("mWifiP2pManager.cancelConnect()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.cancelConnect(mChannel, mActionListenerAdapter);
//	}
//
//	public void onClickCreateGroup(View view) {
//		addMethodLog("mWifiP2pManager.createGroup()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.createGroup(mChannel, mActionListenerAdapter);
//	}
//
//	public void onClickRemoveGroup(View view) {
//		addMethodLog("mWifiP2pManager.removeGroup()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.removeGroup(mChannel, mActionListenerAdapter);
//	}
//
//	public void onClickRequestConnectionInfo(View view) {
//		addMethodLog("mWifiP2pManager.requestConnectionInfo()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.requestConnectionInfo(mChannel,
//				new ConnectionInfoListener() {
//
//					public void onConnectionInfoAvailable(WifiP2pInfo info) {
//						addLog("　onConnectionInfoAvailable():");
//						if (info == null) {
//							addLog("  info is NULL!");
//							return;
//						}
//						addLog("  groupFormed:" + info.groupFormed);
//						addLog("  isGroupOwner:" + info.isGroupOwner);
//						addLog("  groupOwnerAddress:" + info.groupOwnerAddress);
//					}
//				});
//	}
//
//	public void onClickRequestGroupInfo(View view) {
//		addMethodLog("mWifiP2pManager.requestGroupInfo()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.requestGroupInfo(mChannel, new GroupInfoListener() {
//
//			public void onGroupInfoAvailable(WifiP2pGroup group) {
//				addLog("　onGroupInfoAvailable():");
//				if (group == null) {
//					addLog("  group is NULL!");
//					return;
//				}
//
//				String log = separateCSV(group.toString());
//
//				String pass = nl() + "　password: ";
//				if (group.isGroupOwner()) {
//					pass += group.getPassphrase();
//				} else {
//					pass += "Client Couldn't Get Password";
//				}
//				if (HTML_OUT)
//					pass = "<font color=red><b>" + pass + "</b></font>";
//
//				log += pass;
//				if (HTML_OUT)
//					log = "<font color=#fffacd>" + log + "</font>";
//				addLog(log);
//			}
//		});
//	}
//
//	public void onClickRequestPeers(View view) {
//		addMethodLog("mWifiP2pManager.requestPeers()");
//		if (isNull(true)) {
//			return;
//		}
//
//		mWifiP2pManager.requestPeers(mChannel, new PeerListListener() {
//
//			public void onPeersAvailable(WifiP2pDeviceList peers) {
//				mPeers.clear();
//				mPeers.addAll(peers.getDeviceList());
//				int cnt = mPeers.size();
//				addLog("　onPeersAvailable() : num of peers[" + cnt + "]");
//				for (int i = 0; i < cnt; i++) {
//					addLog(nl() + " ***********[" + i + "]***********");
//					addLog("  " + toStringDevice(mPeers.get(i)));
//				}
//
//				updatePeersSpinner();
//			}
//		});
//	}
//
//	private void updatePeersSpinner() {
//
//		if (mPeersSpinner == null) {
//			mPeersSpinner = (Spinner) findViewById(R.id.spinner_peers);
//			mPeersSpinner
//					.setOnItemSelectedListener(new OnItemSelectedListener() {
//						public void onItemSelected(AdapterView<?> parent,
//								View view, int position, long id) {
//							mSelectedDevice = mPeers.get(position).deviceName;
//							addLog(nl() + "Selected Peer[" + mSelectedDevice
//									+ "]");
//						}
//
//						public void onNothingSelected(AdapterView<?> arg0) {
//						}
//					});
//		}
//
//		int cnt = mPeers.size();
//		String[] peers = new String[cnt];
//		for (int i = 0; i < cnt; i++) {
//			peers[i] = mPeers.get(i).deviceName;
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, peers);
//		mPeersSpinner.setAdapter(adapter);
//	}
//
//	public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			String log = "onReceive() [" + action + "]";
//			if (HTML_OUT)
//				log = "<font color=fuchsia>" + log + "</font>";
//			addLog(nl() + log);
//
//			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
//				mIsWiFiDirectEnabled = false;
//				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
//						-1);
//				String sttStr;
//				switch (state) {
//				case WifiP2pManager.WIFI_P2P_STATE_ENABLED:
//					mIsWiFiDirectEnabled = true;
//					sttStr = "ENABLED";
//					break;
//				case WifiP2pManager.WIFI_P2P_STATE_DISABLED:
//					sttStr = "DISABLED";
//					break;
//				default:
//					sttStr = "UNKNOWN";
//					break;
//				}
//				addLog("state[" + sttStr + "](" + state + ")");
//				changeBackgroundColor();
//			} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION
//					.equals(action)) {
//
//				addLog("try requestPeers()");
//			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
//					.equals(action)) {
//				NetworkInfo networkInfo = (NetworkInfo) intent
//						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//
//				String nlog = networkInfo.toString()
//						.replaceAll(",", nl() + "　");
//				if (HTML_OUT)
//					nlog = "<font color=#f0e68c>" + nlog + "</font>";
//				addLog(nlog);
//			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
//					.equals(action)) {
//				WifiP2pDevice device = (WifiP2pDevice) intent
//						.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//				addLog(toStringDevice(device));
//			}
//		}
//	}
//
//	public class WDBR_P2P_STATE_CHANGED_ACTION extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			String log = "onReceive() [" + action + "]";
//			if (HTML_OUT)
//				log = "<font color=fuchsia>" + log + "</font>";
//			addLog(nl() + log);
//
//			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
//				mIsWiFiDirectEnabled = false;
//				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
//						-1);
//				String sttStr;
//				switch (state) {
//				case WifiP2pManager.WIFI_P2P_STATE_ENABLED:
//					mIsWiFiDirectEnabled = true;
//					sttStr = "ENABLED";
//					break;
//				case WifiP2pManager.WIFI_P2P_STATE_DISABLED:
//					sttStr = "DISABLED";
//					break;
//				default:
//					sttStr = "UNKNOWN";
//					break;
//				}
//				addLog("state[" + sttStr + "](" + state + ")");
//				changeBackgroundColor();
//			}
//		}
//	}
//
//	public class WDBR_P2P_PEERS_CHANGED_ACTION extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			String log = "onReceive() [" + action + "]";
//			if (HTML_OUT)
//				log = "<font color=fuchsia>" + log + "</font>";
//			addLog(nl() + log);
//
//			if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
//
//				addLog("try requestPeers()");
//			}
//		}
//	}
//
//	public class WDBR_P2P_CONNECTION_CHANGED_ACTION extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			String log = "onReceive() [" + action + "]";
//			if (HTML_OUT)
//				log = "<font color=fuchsia>" + log + "</font>";
//			addLog(nl() + log);
//
//			if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
//					.equals(action)) {
//				NetworkInfo networkInfo = (NetworkInfo) intent
//						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//
//				String nlog = networkInfo.toString()
//						.replaceAll(",", nl() + "　");
//				if (HTML_OUT)
//					nlog = "<font color=#f0e68c>" + nlog + "</font>";
//				addLog(nlog);
//			}
//		}
//	}
//
//	public class WDBR_P2P_THIS_DEVICE_CHANGED_ACTION extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			String log = "onReceive() [" + action + "]";
//			if (HTML_OUT)
//				log = "<font color=fuchsia>" + log + "</font>";
//			addLog(nl() + log);
//
//			if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
//					.equals(action)) {
//				WifiP2pDevice device = (WifiP2pDevice) intent
//						.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//				addLog(toStringDevice(device));
//			}
//		}
//	}
//
//	private void changeBackgroundColor() {
//		ScrollView sc = (ScrollView) findViewById(R.id.layout_apibuttons);
//		sc.setBackgroundColor(mIsWiFiDirectEnabled ? Color.BLUE : Color.RED);
//	}
//
//	private String getAndroid_ID() {
//		return Settings.Secure.getString(getContentResolver(),
//				Settings.Secure.ANDROID_ID);
//	}
//
//	private String getMACAddress() {
//		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = manager.getConnectionInfo();
//		String mac = wifiInfo.getMacAddress();
//		return mac;
//	}
//
//	private String getAppVersion() {
//		PackageInfo packageInfo = null;
//		try {
//			packageInfo = getPackageManager().getPackageInfo(
//					"com.example.p2p.apitest", PackageManager.GET_META_DATA);
//			String ver = "versionCode : " + packageInfo.versionCode + " / "
//					+ "versionName : " + packageInfo.versionName;
//			return ver;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private boolean hasP2P() {
//		return getPackageManager().hasSystemFeature(
//				PackageManager.FEATURE_WIFI_DIRECT);
//	}
//
//	public void onClickGotoWiFiSetting(View view) {
//		String pac = "com.android.settings";
//		Intent i = new Intent();
//
//		i.setClassName(pac, pac + ".wifi.p2p.WifiP2pSettings");
//		try {
//			startActivity(i);
//		} catch (ActivityNotFoundException e) {
//			Log.e(TAG, "onClickGotoWiFiSetting() " + e.getMessage());
//
//			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH + 1) {
//
//				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//
//			} else {
//				i.setClassName(pac, pac + ".wifi.WifiSettings");
//
//				try {
//					startActivity(i);
//					Toast.makeText(this, "TRY menu -> Wi-Fi Direct",
//							Toast.LENGTH_LONG).show();
//				} catch (ActivityNotFoundException e2) {
//					Log.e(TAG, "onClickGotoWiFiSetting() " + e2.getMessage());
//				}
//			}
//		}
//	}
//
//	public void onClickEnable(View view) {
//		if (isNull(true)) {
//			return;
//		}
//		if (mIsWiFiDirectEnabled) {
//
//			Log.w(TAG, "onClickEnable() Skip disableP2p()");
//		} else {
//
//			Log.w(TAG, "onClickEnable() Skip enableP2p()");
//		}
//	}
//
//}

//package com.example.flightsofangels;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WiFiDirectTestAppActivity extends Activity {
	private final String TAG = "WiFiDirectTestAppActivity";
	private TextView mTextView_Log;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private final String LINE_SEPARATOR_HTML = "<br />";
	private boolean HTML_OUT = true;

	private enum ReceiverState {
		All, StateChange, PeersChange, ConnectionChange, ThisDeviceChange,
	}

	private BroadcastReceiver mReceiver;
	private WDBR_P2P_STATE_CHANGED_ACTION mWDBR_P2P_STATE_CHANGED_ACTION;
	private WDBR_P2P_PEERS_CHANGED_ACTION mWDBR_P2P_PEERS_CHANGED_ACTION;
	private WDBR_P2P_CONNECTION_CHANGED_ACTION mWDBR_P2P_CONNECTION_CHANGED_ACTION;
	private WDBR_P2P_THIS_DEVICE_CHANGED_ACTION mWDBR_THIS_DEVICE_CHANGED_ACTION;
	private boolean mIsWiFiDirectEnabled;
	private WifiP2pManager mWifiP2pManager;
	private Channel mChannel;
	private List<WifiP2pDevice> mPeers = new ArrayList<WifiP2pDevice>();
	private ActionListenerAdapter mActionListenerAdapter;
	private Spinner mPeersSpinner;
	private String mSelectedDevice;
	int choice = 0;
	private boolean f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String title = "ANDROID_ID[" + getAndroid_ID() + "]";
		title += " MAC[" + getMACAddress() + "]";
		setTitle(title);
		initializeLog();
		initBroadcastToggle();
		addLog("onCreate()");
		if (!hasP2P()) {
			toastAndLog("onCreate()", "This Device Has Not P2P Feature!!");
		}
		// new FileServerAsyncTask().execute();
		// final Button loadButton=(Button) findViewById(R.id.button1);
		// loadButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// new HttpGetTask().execute();
		//
		// }
		// });
	}

	public String readFromSocket(Socket socket) throws Exception {

		String response = "";
		try {

			// publishProgress("after socket in client");
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					10000);
			// publishProgress("after byte array socket in client");
			byte buffer[] = new byte[10000];
			// publishProgress("after new byte[1024] in client");

			InputStream inputStream = socket.getInputStream();

			while (true) {
				// publishProgress("after inputstream socket in client");
				if (inputStream.available() == 0) {
					continue;
				}
				int bytesRead = -1;
				// publishProgress("before do in socket in client");
				do {
					bytesRead = inputStream.read(buffer);
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					response += byteArrayOutputStream.toString("UTF-8");
					// publishProgress("Publishing responses...");

					// publishProgress(response);
					bytesRead = -1;
				} while (bytesRead != -1);
				break;

			}
			// publishProgress("after loop socket in client");
		} catch (Exception e) {
			e.printStackTrace();
			WiFiDirectTestAppActivity.this.addLog(e.toString());
			response = "Exception " + e.toString();
		}

		return response;

	}

	public void writeToSocket(Socket socket, String data) throws Exception {
		int size = data.length();
		byte arr[] = new byte[size];
		arr = data.getBytes();
		OutputStream outputStream = socket.getOutputStream();
		// publishProgress("Stream ready to write");
		outputStream.write(arr, 0, arr.length);

		// publishProgress("Written from my side. Baaki doosre ka dekh lo");
	}

	// class FileClientAsyncTask extends AsyncTask<Void, Void, Void> {
	// String destAddress;
	// int destPort;
	// String response;
	//
	// FileClientAsyncTask(String destAddress, int destPort) {
	// this.destAddress = destAddress;
	// this.destPort = destPort;
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// Socket socket = null;
	// try {
	// socket = new Socket(destAddress, destPort);
	// ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
	// 1024);
	// byte buffer[] = new byte[1024];
	// InputStream inputStream = socket.getInputStream();
	// int bytesRead = -1;
	// do {
	// bytesRead = inputStream.read(buffer);
	// byteArrayOutputStream.write(buffer, 0, bytesRead);
	// response += byteArrayOutputStream.toString("UTF-8");
	// } while (bytesRead != -1);
	// } catch (Exception e) {
	// e.printStackTrace();
	// WiFiDirectTestAppActivity.this.addLog(e.toString());
	// response = "Exception " + e.toString();
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(Void result) {
	// WiFiDirectTestAppActivity.this.addLog("In onPostExecute()");
	// super.onPostExecute(result);
	// }
	// }

	class FileClientAsyncTask extends AsyncTask<Void, Void, Void> {
		String destAddress;
		int destPort;
		String response;

		FileClientAsyncTask(String destAddress, int destPort) {
			this.destAddress = destAddress;
			this.destPort = destPort;

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Socket socket = null;
			try {
				socket = new Socket(destAddress, destPort);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
						1024);
				byte buffer[] = new byte[1024];
				InputStream inputStream = socket.getInputStream();
				int bytesRead = -1;
				do {
					bytesRead = inputStream.read(buffer);
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					response += byteArrayOutputStream.toString("UTF-8");
				} while (bytesRead != -1);

			} catch (Exception e) {
				e.printStackTrace();
				WiFiDirectTestAppActivity.this.addLog(e.toString());
				response = "Exception " + e.toString();

			}
			return null;
		}

		protected void onPostExecute(Void result) {
			WiFiDirectTestAppActivity.this.addLog("In onPostExecute()");
			super.onPostExecute(result);
		}

	}

	public void onClickRequestConnectionInfo(View view) {
		addMethodLog("mWifiP2pManager.requestConnectionInfo()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.requestConnectionInfo(mChannel,
				new ConnectionInfoListener() {
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						addLog("　onConnectionInfoAvailable():");
						if (info == null) {
							addLog(" info is NULL!");
							return;
						}
						addLog(" groupFormed:" + info.groupFormed);
						addLog(" isGroupOwner:" + info.isGroupOwner);
						f = info.isGroupOwner;
						addLog(" groupOwnerAddress:" + info.groupOwnerAddress);
					}
				});
	}

	public void execAsync(View view) {
		// AppContext context = new AppContext();
		FileServerAsyncTask obj = new FileServerAsyncTask();
		addLog("Calling Async execute");
		obj.execute();
	}

	public void replyBack(View view) {
		// FileClientAsyncTask obj=new FileClientAsyncTask();
		FileServerAsyncTask obj = new FileServerAsyncTask();
		addLog("Calling Async execute from reply back");
		obj.execute();
	}

	class FileServerAsyncTask extends AsyncTask<Void, String, String> {
		boolean flag = false;
		private Context context;
		private TextView statusText;
		Socket socket;

		// Add a constructor
		// FileServerAsyncTask(Context context, View statusText) {
		// this.context = context;
		// this.statusText = (TextView) statusText;
		// }
		protected void onPreExecute() {
			super.onPreExecute();
			publishProgress("Going to execute now. At onPreExecute");
		}

		@Override
		protected String doInBackground(Void... params) {
			publishProgress("I'm the background task");
			// ServerSocket serverSocket;

			if (f == true) {
				ServerSocket serverSocket = null;
				Socket client = null;
				try {
					publishProgress("I'm the background task inside the try block.");
					serverSocket = new ServerSocket(8888);
					String data = "";
					while (true) {
						publishProgress("Just create the socket!");
						client = serverSocket.accept();
						publishProgress("Socket created");
						// InputStream inputStream = client.getInputStream();
						// String data = "These are the flying angels.";
						EditText editText = (EditText) findViewById(R.id.string_to_transfer);
						data = editText.getText().toString();
						writeToSocket(client, data);
						// int size = 1024;
						// byte arr[] = new byte[1024];
						// arr = data.getBytes();
						// OutputStream outputStream = client.getOutputStream();
						// publishProgress("Stream ready to write");
						// outputStream.write(arr, 0, arr.length);
						publishProgress("Written from my side. Baaki doosre ka dekh lo");
						publishProgress("Start reading from the client...");
						publishProgress(readFromSocket(client));

						client.close();
						if (data.equals(""))// DOES THIS WORK LOL
							break;// Fake break
					}
					return data;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					publishProgress(e.toString());
					publishProgress("Error in socket formation");
					return null;
				}

			} else {
				// Socket socket = null;
				String response = "";

				try {
					//
					// publishProgress("before socket in client");
					socket = new Socket("192.168.49.1", 8888);
					String data = readFromSocket(socket);
					// if (data.equals("`"))
					// {
					// publishProgress("This is tilde.");
					// }
					publishProgress("This is the data read from a client: "
							+ data);

					// final Socket justWork=socket;
					// Create a button and a text field right here. Write to the
					// socket from the client right here.
					if(choice==0)	
					 {writeToSocket(socket, "`");
					 choice =1;
					 }
					 Button createBtn = (Button) findViewById(R.id.button2);

					createBtn.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Context context = getApplicationContext();
							String text = ((EditText) findViewById(R.id.string_to_transfer))
									.getText().toString();
							int duration = Toast.LENGTH_SHORT;

							try {
								publishProgress("Going to reply to GO with "
										+ text);
								publishProgress("Before choice computations begin " + choice);
								//if (choice == 0) {
									//writeToSocket(socket, "~");
									//publishProgress("in ch ==0" + choice);
									//choice=1;
									//publishProgress("in ch ==0 set choice as" + choice);
								//}
								if(choice ==1)
								{
									writeToSocket(socket, text);
									publishProgress("Data has been written.");
									publishProgress("in ch ==0 else parts" + choice);
									choice = 0;
									publishProgress("set ch =1 in else part only " + choice);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								publishProgress("Exception in replying");
								e.printStackTrace();
							}

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.show();
						}
					});

					publishProgress("Return. ");
					// socket.close();
					// publishProgress("after socket in client");
					// ByteArrayOutputStream byteArrayOutputStream = new
					// ByteArrayOutputStream(
					// 1024);
					// publishProgress("after byte array socket in client");
					// byte buffer[] = new byte[1024];
					// publishProgress("after new byte[1024] in client");
					// InputStream inputStream = socket.getInputStream();
					// publishProgress("after inputstream socket in client");
					// int bytesRead = -1;
					// publishProgress("before do in socket in client");
					// do {
					// bytesRead = inputStream.read(buffer);
					// byteArrayOutputStream.write(buffer, 0, bytesRead);
					// response += byteArrayOutputStream.toString("UTF-8");
					// publishProgress("Publishing responses...");
					//
					// publishProgress(response);
					// bytesRead = -1;
					// } while (bytesRead != -1);
					// publishProgress("after loop socket in client");
				} catch (Exception e) {
					e.printStackTrace();
					WiFiDirectTestAppActivity.this.addLog(e.toString());
					response = "Exception " + e.toString();

				}
			}

			return null;

			// 8826621278
		}

		protected void onProgressUpdate(String... params) {
			WiFiDirectTestAppActivity.this.addLog(params[0]);
		}

		protected void onPostExecute(String result) {
			if (result != null) {
				WiFiDirectTestAppActivity.this
						.addLog("Beeg American Teetees!!!");
				WiFiDirectTestAppActivity.this
						.addLog("String sent successfully!!");
				// Intent intent = new Intent();
				// intent.setAction(android.content.Intent.ACTION_VIEW);
				// context.startActivity(intent);
			} else
				;
			// publishProgress("Not Transfered");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		addLog("onResume()");
		mIsWiFiDirectEnabled = false;
		registerBroadcastReceiver(ReceiverState.All);
	}

	@Override
	protected void onPause() {
		super.onPause();
		addLog("onPause()");
		unRegisterBroadcastReceiver(ReceiverState.All);
	}

	private void registerBroadcastReceiver(ReceiverState rs) {
		IntentFilter filter = new IntentFilter();
		switch (rs) {
		case All:
			filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
			filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
			filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
			filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
			mReceiver = new WiFiDirectBroadcastReceiver();
			registerReceiver(mReceiver, filter);
			addLog("registerBroadcastReceiver() BroadcastReceiver");
			break;
		case StateChange:
			filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
			mWDBR_P2P_STATE_CHANGED_ACTION = new WDBR_P2P_STATE_CHANGED_ACTION();
			registerReceiver(mWDBR_P2P_STATE_CHANGED_ACTION, filter);
			addLog("registerBroadcastReceiver() P2P_STATE_CHANGED_ACTION");
			break;
		case PeersChange:
			filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
			mWDBR_P2P_PEERS_CHANGED_ACTION = new WDBR_P2P_PEERS_CHANGED_ACTION();
			registerReceiver(mWDBR_P2P_PEERS_CHANGED_ACTION, filter);
			addLog("registerBroadcastReceiver() P2P_PEERS_CHANGED_ACTION");
			break;
		case ConnectionChange:
			filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
			mWDBR_P2P_CONNECTION_CHANGED_ACTION = new WDBR_P2P_CONNECTION_CHANGED_ACTION();
			registerReceiver(mWDBR_P2P_CONNECTION_CHANGED_ACTION, filter);
			addLog("registerBroadcastReceiver() P2P_CONNECTION_CHANGED_ACTION");
			break;
		case ThisDeviceChange:
			filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
			mWDBR_THIS_DEVICE_CHANGED_ACTION = new WDBR_P2P_THIS_DEVICE_CHANGED_ACTION();
			registerReceiver(mWDBR_THIS_DEVICE_CHANGED_ACTION, filter);
			addLog("registerBroadcastReceiver() THIS_DEVICE_CHANGED_ACTION");
			break;
		default:
			toastAndLog("registerBroadcastReceiver()", "Unknown ReceiverState["
					+ rs + "]");
			break;
		}
	}

	private void unRegisterBroadcastReceiver(ReceiverState rs) {
		switch (rs) {
		case All:
			if (mReceiver != null) {
				unregisterReceiver(mReceiver);
				mReceiver = null;
				addLog("unRegisterBroadcastReceiver() BroadcastReceiver");
			}
			break;
		case StateChange:
			if (mWDBR_P2P_STATE_CHANGED_ACTION != null) {
				unregisterReceiver(mWDBR_P2P_STATE_CHANGED_ACTION);
				mWDBR_P2P_STATE_CHANGED_ACTION = null;
				addLog("unRegisterBroadcastReceiver() P2P_STATE_CHANGED_ACTION");
			}
			break;
		case PeersChange:
			if (mWDBR_P2P_PEERS_CHANGED_ACTION != null) {
				unregisterReceiver(mWDBR_P2P_PEERS_CHANGED_ACTION);
				mWDBR_P2P_PEERS_CHANGED_ACTION = null;
				addLog("unRegisterBroadcastReceiver() P2P_PEERS_CHANGED_ACTION");
			}
			break;
		case ConnectionChange:
			if (mWDBR_P2P_CONNECTION_CHANGED_ACTION != null) {
				unregisterReceiver(mWDBR_P2P_CONNECTION_CHANGED_ACTION);
				mWDBR_P2P_CONNECTION_CHANGED_ACTION = null;
				addLog("unRegisterBroadcastReceiver() P2P_CONNECTION_CHANGED_ACTION");
			}
			break;
		case ThisDeviceChange:
			if (mWDBR_THIS_DEVICE_CHANGED_ACTION != null) {
				unregisterReceiver(mWDBR_THIS_DEVICE_CHANGED_ACTION);
				mWDBR_THIS_DEVICE_CHANGED_ACTION = null;
				addLog("unRegisterBroadcastReceiver() THIS_DEVICE_CHANGED_ACTION");
			}
			break;
		default:
			toastAndLog("unRegisterBroadcastReceiver()",
					"Unknown ReceiverState[" + rs + "]");
			break;
		}
	}

	private void initBroadcastToggle() {
		initBroadcastToggleInner(R.id.toggle_bc_all);
		initBroadcastToggleInner(R.id.toggle_bc_state);
		initBroadcastToggleInner(R.id.toggle_bc_peers);
		initBroadcastToggleInner(R.id.toggle_bc_connection);
		initBroadcastToggleInner(R.id.toggle_bc_this);
	}

	private void initBroadcastToggleInner(final int rId_Toggle) {
		ToggleButton tb = (ToggleButton) findViewById(rId_Toggle);
		tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ReceiverState rs = ReceiverState.All;
				switch (rId_Toggle) {
				case R.id.toggle_bc_all:
					rs = ReceiverState.All;
					break;
				case R.id.toggle_bc_state:
					rs = ReceiverState.StateChange;
					break;
				case R.id.toggle_bc_peers:
					rs = ReceiverState.PeersChange;
					break;
				case R.id.toggle_bc_connection:
					rs = ReceiverState.ConnectionChange;
					break;
				case R.id.toggle_bc_this:
					rs = ReceiverState.ThisDeviceChange;
					break;
				default:
					toastAndLog("initBroadcastToggleInner()",
							"Unknown ReceiverState[" + rs + "]");
					return;
				}
				if (isChecked) {
					registerBroadcastReceiver(rs);
				} else {
					unRegisterBroadcastReceiver(rs);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, getString(R.string.app_about));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		int id = item.getItemId();
		switch (id) {
		default:
			toastAndLog("onOptionsItemSelected()", "Unknown Item Id[" + id
					+ "]");
			break;
		case 0:
			Toast.makeText(this, getAppVersion(), Toast.LENGTH_SHORT).show();
			break;
		}
		return ret;
	}

	private void initializeLog() {
		if (mTextView_Log != null) {
			return;
		}
		mTextView_Log = (TextView) findViewById(R.id.textView_log);
		mTextView_Log.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				ToggleButton tb = (ToggleButton) findViewById(R.id.toggle_autoscroll);
				if (!tb.isChecked()) {
					return;
				}
				final ScrollView sv = (ScrollView) findViewById(R.id.scrollview_log);
				sv.post(new Runnable() {
					public void run() {
						sv.fullScroll(View.FOCUS_DOWN);
					}
				});
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup_logkind);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radiobutton_html:
					HTML_OUT = true;
					break;
				case R.id.radiobutton_mono:
					HTML_OUT = false;
					break;
				default:
					addLog("initializeLog() Unknown Log Kind[" + checkedId
							+ "]");
					HTML_OUT = false;
					break;
				}
			}
		});
	}

	private void addLog(String log) {
		Log.d(TAG, log);
		log = log + nl();
		if (mTextView_Log == null) {
			initializeLog();
		}
		mTextView_Log.append(HTML_OUT ? convHtmlStr2CS(log) : log);
	}

	private CharSequence convHtmlStr2CS(String htmlStr) {
		return Html.fromHtml(htmlStr);
	}

	private String nl() {
		return HTML_OUT ? LINE_SEPARATOR_HTML : LINE_SEPARATOR;
	}

	private void addMethodLog(String method) {
		if (HTML_OUT)
			method = "<font color=lime>" + method + "</font>";
		addLog(nl() + method);
	}

	private void toastAndLog(String msg1, String msg2) {
		String log = msg1 + LINE_SEPARATOR + msg2;
		Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
		if (HTML_OUT)
			log = "<font color=red>" + msg1 + nl() + msg2 + "</font>";
		addLog(log);
	}

	public void onClickResetLog(View view) {
		mTextView_Log.setText("");
	}

	public void onClickSaveLog(View view) {
		String log = mTextView_Log.getText().toString();
		Log.d(TAG, "onClickSaveLog() LOG[" + log + "]");
	}

	private String toStringDevice(WifiP2pDevice device) {
		String log = separateCSV(device.toString()) + nl() + "　"
				+ getDeviceStatus(device.status);
		return HTML_OUT ? "<font color=yellow>" + log + "</font>" : log;
	}

	private String separateCSV(String csvStr) {
		return csvStr.replaceAll("[^:yWFD] ", nl() + "　");
	}

	private String getDeviceStatus(int deviceStatus) {
		String status = "";
		switch (deviceStatus) {
		case WifiP2pDevice.AVAILABLE:
			status = "Available";
			break;
		case WifiP2pDevice.INVITED:
			status = "Invited";
			break;
		case WifiP2pDevice.CONNECTED:
			status = "Connected";
			break;
		case WifiP2pDevice.FAILED:
			status = "Failed";
			break;
		case WifiP2pDevice.UNAVAILABLE:
			status = "Unavailable";
			break;
		default:
			status = "Unknown";
			break;
		}
		return HTML_OUT ? "[<b><i><u>" + status + "</u></i></b>]" : "["
				+ status + "]";
	}

	class ActionListenerAdapter implements WifiP2pManager.ActionListener {
		public void onSuccess() {
			String log = " onSuccess()";
			if (HTML_OUT)
				log = "<font color=aqua>　" + log + "</font>";
			addLog(log);
		}

		public void onFailure(int reason) {
			String log = " onFailure(" + getReason(reason) + ")";
			if (HTML_OUT)
				log = "<font color=red>　" + log + "</font>";
			addLog(log);
		}

		private String getReason(int reason) {
			String[] strs = { "ERROR", "P2P_UNSUPPORTED", "BUSY" };
			try {
				return strs[reason] + "(" + reason + ")";
			} catch (ArrayIndexOutOfBoundsException e) {
				return "UNKNOWN REASON CODE(" + reason + ")";
			}
		}
	}

	private boolean isNull(boolean both) {
		if (mActionListenerAdapter == null) {
			mActionListenerAdapter = new ActionListenerAdapter();
		}
		if (!mIsWiFiDirectEnabled) {
			toastAndLog(" Wi-Fi Direct is OFF!", "try Setting Menu");
			return true;
		}
		if (mWifiP2pManager == null) {
			toastAndLog(" mWifiP2pManager is NULL!", " try getSystemService");
			return true;
		}
		if (both && (mChannel == null)) {
			toastAndLog(" mChannel is NULL!", " try initialize");
			return true;
		}
		return false;
	}

	public void onClickGetSystemService(View view) {
		addMethodLog("getSystemService(Context.WIFI_P2P_SERVICE)");
		mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		addLog("　Result[" + (mWifiP2pManager != null) + "]");
	}

	@SuppressLint("NewApi")
	public void onClickInitialize(View view) {
		addMethodLog("mWifiP2pManager.initialize()");
		if (isNull(false)) {
			return;
		}
		mChannel = mWifiP2pManager.initialize(this, getMainLooper(),
				new ChannelListener() {
					public void onChannelDisconnected() {
						addLog("mWifiP2pManager.initialize() -> onChannelDisconnected()");
					}
				});
		addLog("　Result[" + (mChannel != null) + "]");
	}

	public void onClickDiscoverPeers(View view) {
		addMethodLog("mWifiP2pManager.discoverPeers()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.discoverPeers(mChannel, mActionListenerAdapter);
	}

	public void onClickConnect(View view) {
		addMethodLog("mWifiP2pManager.connect()");
		if (isNull(true)) {
			return;
		}
		int cnt = mPeers.size();
		if (cnt == 0) {
			addLog(" peer not found! try discoverPeers & requestPeers");
			return;
		}
		int idx = 0;
		for (WifiP2pDevice device : mPeers) {
			if (device.deviceName.equals(mSelectedDevice)) {
				break;
			}
			idx += 1;
		}
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = mPeers.get(idx).deviceAddress;
		config.groupOwnerIntent = getOwnerIntentValue();
		config.wps.setup = getWPSSetupValue();
		addLog(" connecting to [" + mSelectedDevice + "]["
				+ config.deviceAddress + "] G.O.[" + config.groupOwnerIntent
				+ "] WPS[" + config.wps.setup + "]");
		mWifiP2pManager.connect(mChannel, config, mActionListenerAdapter);
	}

	private int getOwnerIntentValue() {
		Spinner sp = (Spinner) findViewById(R.id.spinner_go);
		int v = sp.getSelectedItemPosition();
		return v - 1;
	}

	private int getWPSSetupValue() {
		Spinner sp = (Spinner) findViewById(R.id.spinner_wps);
		int v = sp.getSelectedItemPosition();
		switch (v) {
		case 0:
			return WpsInfo.PBC;
		case 1:
			return WpsInfo.DISPLAY;
		case 2:
			return WpsInfo.KEYPAD;
		case 3:
			return WpsInfo.LABEL;
		case 4:
			return WpsInfo.INVALID;
		default:
			toastAndLog("getWPSSetupValue()", "Unknown WPS Index[" + v + "]");
			return WpsInfo.INVALID;
		}
	}

	public void onClickCancelConnect(View view) {
		addMethodLog("mWifiP2pManager.cancelConnect()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.cancelConnect(mChannel, mActionListenerAdapter);
	}

	public void onClickCreateGroup(View view) {
		addMethodLog("mWifiP2pManager.createGroup()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.createGroup(mChannel, mActionListenerAdapter);
	}

	public void onClickRemoveGroup(View view) {
		addMethodLog("mWifiP2pManager.removeGroup()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.removeGroup(mChannel, mActionListenerAdapter);
	}

	public void onClickRequestGroupInfo(View view) {
		addMethodLog("mWifiP2pManager.requestGroupInfo()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.requestGroupInfo(mChannel, new GroupInfoListener() {
			public void onGroupInfoAvailable(WifiP2pGroup group) {
				addLog("　onGroupInfoAvailable():");
				if (group == null) {
					addLog(" group is NULL!");
					return;
				}
				String log = separateCSV(group.toString());
				String pass = nl() + "　password: ";
				if (group.isGroupOwner()) {
					pass += group.getPassphrase();
				} else {
					pass += "Client Couldn't Get Password";
				}
				if (HTML_OUT)
					pass = "<font color=red><b>" + pass + "</b></font>";
				log += pass;
				if (HTML_OUT)
					log = "<font color=#fffacd>" + log + "</font>";
				addLog(log);
			}
		});
	}

	public void onClickRequestPeers(View view) {
		addMethodLog("mWifiP2pManager.requestPeers()");
		if (isNull(true)) {
			return;
		}
		mWifiP2pManager.requestPeers(mChannel, new PeerListListener() {
			public void onPeersAvailable(WifiP2pDeviceList peers) {
				mPeers.clear();
				mPeers.addAll(peers.getDeviceList());
				int cnt = mPeers.size();
				addLog("　onPeersAvailable() : num of peers[" + cnt + "]");
				for (int i = 0; i < cnt; i++) {
					addLog(nl() + " ***********[" + i + "]***********");
					addLog(" " + toStringDevice(mPeers.get(i)));
				}
				updatePeersSpinner();
			}
		});
	}

	private void updatePeersSpinner() {
		if (mPeersSpinner == null) {
			mPeersSpinner = (Spinner) findViewById(R.id.spinner_peers);
			mPeersSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							mSelectedDevice = mPeers.get(position).deviceName;
							addLog(nl() + "Selected Peer[" + mSelectedDevice
									+ "]");
						}

						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}
		int cnt = mPeers.size();
		String[] peers = new String[cnt];
		for (int i = 0; i < cnt; i++) {
			peers[i] = mPeers.get(i).deviceName;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, peers);
		mPeersSpinner.setAdapter(adapter);
	}

	public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String log = "onReceive() [" + action + "]";
			if (HTML_OUT)
				log = "<font color=fuchsia>" + log + "</font>";
			addLog(nl() + log);
			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
				mIsWiFiDirectEnabled = false;
				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
						-1);
				String sttStr;
				switch (state) {
				case WifiP2pManager.WIFI_P2P_STATE_ENABLED:
					mIsWiFiDirectEnabled = true;
					sttStr = "ENABLED";
					break;
				case WifiP2pManager.WIFI_P2P_STATE_DISABLED:
					sttStr = "DISABLED";
					break;
				default:
					sttStr = "UNKNOWN";
					break;
				}
				addLog("state[" + sttStr + "](" + state + ")");
				changeBackgroundColor();
			} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION
					.equals(action)) {
				addLog("try requestPeers()");
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
					.equals(action)) {
				NetworkInfo networkInfo = (NetworkInfo) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				String nlog = networkInfo.toString()
						.replaceAll(",", nl() + "　");
				if (HTML_OUT)
					nlog = "<font color=#f0e68c>" + nlog + "</font>";
				addLog(nlog);
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
					.equals(action)) {
				WifiP2pDevice device = (WifiP2pDevice) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
				addLog(toStringDevice(device));
			}
		}
	}

	public class WDBR_P2P_STATE_CHANGED_ACTION extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String log = "onReceive() [" + action + "]";
			if (HTML_OUT)
				log = "<font color=fuchsia>" + log + "</font>";
			addLog(nl() + log);
			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
				mIsWiFiDirectEnabled = false;
				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
						-1);
				String sttStr;
				switch (state) {
				case WifiP2pManager.WIFI_P2P_STATE_ENABLED:
					mIsWiFiDirectEnabled = true;
					sttStr = "ENABLED";
					break;
				case WifiP2pManager.WIFI_P2P_STATE_DISABLED:
					sttStr = "DISABLED";
					break;
				default:
					sttStr = "UNKNOWN";
					break;
				}
				addLog("state[" + sttStr + "](" + state + ")");
				changeBackgroundColor();
			}
		}
	}

	public class WDBR_P2P_PEERS_CHANGED_ACTION extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String log = "onReceive() [" + action + "]";
			if (HTML_OUT)
				log = "<font color=fuchsia>" + log + "</font>";
			addLog(nl() + log);
			if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
				addLog("try requestPeers()");
			}
		}
	}

	public class WDBR_P2P_CONNECTION_CHANGED_ACTION extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String log = "onReceive() [" + action + "]";
			if (HTML_OUT)
				log = "<font color=fuchsia>" + log + "</font>";
			addLog(nl() + log);
			if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
					.equals(action)) {
				NetworkInfo networkInfo = (NetworkInfo) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				String nlog = networkInfo.toString()
						.replaceAll(",", nl() + "　");
				if (HTML_OUT)
					nlog = "<font color=#f0e68c>" + nlog + "</font>";
				addLog(nlog);
			}
		}
	}

	public class WDBR_P2P_THIS_DEVICE_CHANGED_ACTION extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String log = "onReceive() [" + action + "]";
			if (HTML_OUT)
				log = "<font color=fuchsia>" + log + "</font>";
			addLog(nl() + log);
			if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
					.equals(action)) {
				WifiP2pDevice device = (WifiP2pDevice) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
				addLog(toStringDevice(device));
			}
		}
	}

	private void changeBackgroundColor() {
		ScrollView sc = (ScrollView) findViewById(R.id.layout_apibuttons);
		sc.setBackgroundColor(mIsWiFiDirectEnabled ? Color.BLUE : Color.RED);
	}

	private String getAndroid_ID() {
		return Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	private String getMACAddress() {
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();
		String mac = wifiInfo.getMacAddress();
		return mac;
	}

	private String getAppVersion() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(
					"com.example.p2p.apitest", PackageManager.GET_META_DATA);
			String ver = "versionCode : " + packageInfo.versionCode + " / "
					+ "versionName : " + packageInfo.versionName;
			return ver;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean hasP2P() {
		return getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_WIFI_DIRECT);
	}

	public void onClickGotoWiFiSetting(View view) {
		String pac = "com.android.settings";
		Intent i = new Intent();
		i.setClassName(pac, pac + ".wifi.p2p.WifiP2pSettings");
		try {
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "onClickGotoWiFiSetting() " + e.getMessage());
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH + 1) {
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			} else {
				i.setClassName(pac, pac + ".wifi.WifiSettings");
				try {
					startActivity(i);
					Toast.makeText(this, "TRY menu -> Wi-Fi Direct",
							Toast.LENGTH_LONG).show();
				} catch (ActivityNotFoundException e2) {
					Log.e(TAG, "onClickGotoWiFiSetting() " + e2.getMessage());
				}
			}
		}
	}

	public void onClickEnable(View view) {
		if (isNull(true)) {
			return;
		}
		if (mIsWiFiDirectEnabled) {
			Log.w(TAG, "onClickEnable() Skip disableP2p()");
		} else {
			Log.w(TAG, "onClickEnable() Skip enableP2p()");
		}
	}
}
