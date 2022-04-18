import requests
import random
import string
import socket


BASE_URL = f"http://localhost:7070"
# BASE_URL = f"http://{socket.gethostname()}.local:7070" # WSL 2 (doesn't always work)

print(BASE_URL)


def random_string(total_len: int = 16, prefix: str = "TEST_") -> str:
     return prefix + ''.join(random.choice(string.ascii_letters) for i in range(total_len-len(prefix)))


def create_user(user_id: str = None, token: str = None):
     if user_id is None:
          user_id = random_string()
     if token is None:
          token = random_string()

     response = requests.post(f"{BASE_URL}/token", data={"userID": user_id, "token": token})
     assert response.status_code == 200
     return user_id

def create_lobby(user_id: str):
     response = requests.post(f"{BASE_URL}/create_lobby", data={"userID": user_id})
     assert response.status_code == 200
     return response.text.split("|")[0]


def join_lobby(user_id: str, invite_id: str):
     response = requests.post(f"{BASE_URL}/join_lobby", data={"userID": user_id, "id": invite_id})
     assert response.status_code == 200

def join_matchmaking(user_id: str):
     response = requests.post(f"{BASE_URL}/join_matchmaking", data={"userID": user_id})
     assert response.status_code == 200


if __name__ == "__main__":
     user1 = create_user()
     user2 = create_user()

     invite_code = create_lobby(user1)
     print(invite_code)

     join_lobby(user2, invite_code)

     join_matchmaking(user1)
     join_matchmaking(user2)

