package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class SharedBox {
	String text;
	List<EchoRunnable2> clients = new ArrayList<EchoRunnable2>();
	
	public void addClient(EchoRunnable2 client) {
		clients.add(client);
		System.out.println("클라이언트 추가");
	}
	
	public void broadcast(String str) {
		for(EchoRunnable2 er : clients) {
			System.out.println("방송 합니다.");
			er.out(str,1);
		}
	}
	public void broadcast2(String str) {
		for(EchoRunnable2 er : clients) {
			System.out.println("방송 합니다.");
			er.out(str,3);
		}
	}
}

class WaitingRoom{
	Exam99_RoomManager roomManager = new Exam99_RoomManager();
	List<EchoRunnable2> clients = new ArrayList<EchoRunnable2>();
	
	public void addClient(EchoRunnable2 client) {
		clients.add(client);
	}
	
}

class EchoRunnable2 implements Runnable{
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	SharedBox sharedBox;
	String ID;
	Exam99_RoomManager roomManager;
	int state = 0;
	final static int waittingRoomState = 1;
	final static int chattingRoomState = 2;
	
	
	
	EchoRunnable2 (Socket socket , SharedBox sharedBox,Exam99_RoomManager roomManager){
		this.socket = socket;
		this.sharedBox = sharedBox;
		this.roomManager = roomManager;
		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(isr);
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return this.state;
	}
	public void setID(String str) {
		this.ID = str;
	}
	public String getID() {
		return ID;
	}
	public void out(String str , int state) {
		System.out.println("클라이언트에게 값을 넘겨줍니다. : " + str);
		if(state == 3) { // 외치기
			out.println("CHAT2,"+this.state+","+str);
			out.flush();
		} else if(this.state == state) {
			out.println(str);
			out.flush();
		}
		
	}
	
	@Override
	public void run() {
		String line;
		System.out.println("클라이언트 쓰래드가 돌고 있어요~");
		
		try {
			Exam99_Room room;
			while((line = br.readLine()) != null) {
				System.out.println("클라이언트 데이터 받기 : " + line);
				// br.readLine() 이 null 인 경우 는 클라이언트가 접속을 끊었을때
				String receiveDateFromClient[] = line.split(",");
				String protocol = receiveDateFromClient[0];
				
				if(protocol.equals("/@EXIT")) {
					break;// 가장 근접한 루프를 탈출
				}
				
				if(protocol.equals("/@ENRO")){
					String roomNum = receiveDateFromClient[1];
					System.out.println("방번호는 : " + roomNum);
					if(!roomManager.isExitRoom(roomNum)) {
						// 기존에 룸이 없으면
						System.out.println("여기 가면 안되는데...");
						room = new Exam99_Room(roomNum);
						room.addClient(this);
						roomManager.addRoom(room);
						System.out.println("채팅방에 클라이언트를 넣었어요. : " + room.getRoomNum());
					}else {
						// 기존에 룸이 있으면
						room = roomManager.getRoom(roomNum);
						room.addClient(this);
						System.out.println("채팅방에 클라이언트를 넣었어요. : " + room.getRoomNum());
					}
					
				} else if(protocol.equals("/@CLID")) {
					this.ID = receiveDateFromClient[1];
					out.println(roomManager.getStringRoomList());// 룸리스트
					out.flush();
					System.out.println("룸리스트 : " + roomManager.getStringRoomList());
					
				} else if (protocol.equals("/@MKRO")) {
					System.out.println("방만들고 방 리스트를 주자");
					System.out.println(receiveDateFromClient[0]+receiveDateFromClient[1]);
					Exam99_Room newRoom = new Exam99_Room(roomManager.makeRoomNum()+"");
					newRoom.setRoomTitle(receiveDateFromClient[1]);
					roomManager.addRoom(newRoom);
					System.out.println("룸리스트 : " + roomManager.getStringRoomList());
					sharedBox.broadcast(roomManager.getStringRoomList());
					
				} else if (protocol.equals("/@CHAT")) {
					System.out.println("채팅메세지야~");
					room = roomManager.getRoom(receiveDateFromClient[1]);
					room.broadcast(receiveDateFromClient[2]);
				} else if (protocol.equals("/@STATE")) {
					int newState = Integer.parseInt(receiveDateFromClient[1]);
					if(state == 2 && newState == 1) {
						// 방나가기.
						System.out.println("방나가기");
						room = roomManager.getRoom(receiveDateFromClient[2]);
						room.exitRoom(this.ID);
						out(roomManager.getStringRoomList(),2);
					}
					this.state = newState;
					System.out.println("클라이언트의 상태가 바꼈어요 : "+ receiveDateFromClient[1]);
				} else if (protocol.equals("/@ROLI")) {
					System.out.println("룸리스트 : " + roomManager.getStringRoomList());
					sharedBox.broadcast(roomManager.getStringRoomList());
				} else if (protocol.equals("/@CHAT2")) {
					System.out.println("외치기 기능이야");
					sharedBox.broadcast2(receiveDateFromClient[2]);
				}
			}
			// 자원 정리...
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

public class Exam99_Server extends Application{
	TextArea textArea;
	Button startBtn , closeBtn;
	ExecutorService executorService = Executors.newCachedThreadPool();
	ServerSocket server;
	TextField textField;
	List<EchoRunnable2> clients = new ArrayList<EchoRunnable2>();
	Exam99_RoomManager roomManager = new Exam99_RoomManager();
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg + "\n");
		});
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500); 
		SharedBox sharedBox = new SharedBox();
		
		textArea = new TextArea();
		textField = new TextField();
		textField.setPrefSize(400, 100);
		root.setCenter(textArea);
		
		startBtn = new Button("서버시작.");
		startBtn.setPrefSize(250, 100);
		startBtn.setOnAction(t->{
			Runnable runnable = ()->{
				try {
					//버튼을 누르면 서버 쪽에 server쪽에 socket접속을 시도.
					server = new ServerSocket(7878);
					printMsg("에코서버 시작!");
					while(true) {
						printMsg("클라이언트 접속 대기\n");
						Socket s = server.accept(); // 여기에서 블락 클라이언트 올때 까지 -> 그래서 자바fx가 멈춤
						// 그래서 여기 대기하는 부분을 새로운 쓰래드로 만들어야함
						
						printMsg("클라이언트 접속 성공\n");
						// 클라이언트가 접속햇으니 쓰래드 만들고 쓰래드 시작.
						EchoRunnable2 r = new EchoRunnable2(s,sharedBox,roomManager);
						System.out.println("클라이언트 쓰래드 생성\n");
						sharedBox.addClient(r);
						executorService.execute(r);
					}
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
			executorService.execute(runnable);
		});
		
		
		closeBtn = new Button("echo 서버 접속 해제");
		closeBtn.setPrefSize(250, 100);
		closeBtn.setOnAction(t->{
			try {
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(textField);
		flowPane.getChildren().add(closeBtn);
		root.setBottom(flowPane);
		

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("서버입니다.");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		
		launch();
	}
}
