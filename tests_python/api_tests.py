import requests
import random
import string
import socket


PRODUCTION = f"https://battleships.borysek.eu"  # WARNING: Some tests will be skipped for production.

BASE_URL = f"http://localhost:7070"
BASE_URL = PRODUCTION
# BASE_URL = f"http://{socket.gethostname()}.local:7070" # WSL 2 (doesn't always work)

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

def join_matchmaking(user_id: str, private_lobby=True):
     response = requests.post(f"{BASE_URL}/join_matchmaking", data={"userID": user_id, "privateLobby": private_lobby})
     assert response.status_code == 200


def test_machmaking():
     user1 = create_user()
     user2 = create_user()

     invite_code = create_lobby(user1)
     print(invite_code)

     join_lobby(user2, invite_code)

     join_matchmaking(user1, True)
     join_matchmaking(user2, True)

     join_matchmaking(user1, False)
     join_matchmaking(user2, False)


def play_game_as_a_player():
     user_id = create_user()

     lobby_id = int(input("Input lobby id: "))
     join_lobby(user_id, lobby_id)

     input("Press key to place the ships")
     ship_placement(user_id)

     for i in range(10):
          input("Press key to shoot a single shot")
          single_shot(user_id, i)


def ship_placement(user_id: int):
     placement_str = "rO0ABXNyAENuby5udG51LnRkdDQyNDAueTIwMjIuZ3JvdXAyMy5iYXR0bGVzaGlwc2dhbWUuTW9kZWxzLlNoaXBQbGFjZW1lbnRzUUXbXd60vCgCAAFMAAVzaGlwc3QAEExqYXZhL3V0aWwvTGlzdDt4cHNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAABc3IAQ25vLm50bnUudGR0NDI0MC55MjAyMi5ncm91cDIzLmJhdHRsZXNoaXBzZ2FtZS5TaGlwcy5SZWN0YW5ndWxhclNoaXDlN0vA0+uL2AIAAHhyAEBuby5udG51LnRkdDQyNDAueTIwMjIuZ3JvdXAyMy5iYXR0bGVzaGlwc2dhbWUuU2hpcHMuQWJzdHJhY3RTaGlw4ccxYFJJsk0CAANaAApob3Jpem9udGFsSQAFcGFydHNMAAlwb3NpdGlvbnNxAH4AAXhwAQAAAANzcQB+AAMAAAADdwQAAAADc3IAO25vLm50bnUudGR0NDI0MC55MjAyMi5ncm91cDIzLmJhdHRsZXNoaXBzZ2FtZS5Nb2RlbHMuQ29vcmRz91HrauddSFUCAAJJAAF4SQABeXhwAAAABQAAAAVzcQB+AAkAAAAGAAAABXNxAH4ACQAAAAcAAAAFeHg="
     response = requests.post(f"{BASE_URL}/placements", data={"userID": user_id, "placements": placement_str})
     assert response.status_code == 200

def single_shot(user_id: int, shot_id: int):
     single_shot = "rO0ABXNyAEBuby5udG51LnRkdDQyNDAueTIwMjIuZ3JvdXAyMy5iYXR0bGVzaGlwc2dhbWUuQWN0aW9ucy5TaW5nbGVTaG90mUUJKvowoG8CAAB4cgBEbm8ubnRudS50ZHQ0MjQwLnkyMDIyLmdyb3VwMjMuYmF0dGxlc2hpcHNnYW1lLkFjdGlvbnMuQWJzdHJhY3RBY3Rpb27Ft0fYnd5DrgIAAUwABmNvb3Jkc3QAPUxuby9udG51L3RkdDQyNDAveTIwMjIvZ3JvdXAyMy9iYXR0bGVzaGlwc2dhbWUvTW9kZWxzL0Nvb3Jkczt4cHNyADtuby5udG51LnRkdDQyNDAueTIwMjIuZ3JvdXAyMy5iYXR0bGVzaGlwc2dhbWUuTW9kZWxzLkNvb3Jkc/dR62rnXUhVAgACSQABeEkAAXl4cAAAAAEAAAAB"

     last_char = chr(ord(single_shot[-1])+shot_id)

     response = requests.post(f"{BASE_URL}/placements", data={"userID": user_id, "action": single_shot[:-1] + last_char})
     assert response.status_code == 200


if __name__ == "__main__":
     print(BASE_URL)

     if BASE_URL != PRODUCTION:
          test_machmaking()

     response = requests.get(f"{BASE_URL}/healthcheck")
     print(response.json())
     assert response.status_code == 200

     play_game_as_a_player()


