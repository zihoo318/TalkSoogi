import sys
from basicAnalysis import ChatRoom

def main():
    if len(sys.argv) < 2:
        print("Usage: python main_script.py <file_path>")
        sys.exit(1)

    file_path = sys.argv[1]
    chat_room = ChatRoom(file_path)
    chat_room.preprocess_and_analyze()

    print(f"채팅방 이름: {chat_room.room_name}")
    print(f"멤버들: {', '.join(chat_room.get_members())}")
    print(f"채팅방 인원 수: {chat_room.get_member_count()}")

if __name__ == "__main__":
    main()
