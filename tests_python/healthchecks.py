import requests

def send_notification(suffix: str = ""):
    # security: this url shouldn't be in public repository, but our repository won't be made public
    requests.get("https://healthchecks.borysek.eu/ping/8764edbf-3520-4776-a391-868ce53d005d"+suffix, timeout=10)

send_notification("/start")

resp = requests.get("https://battleships.borysek.eu/healthcheck")
assert resp.status_code == 200
data = resp.json()

if len(list(filter(lambda x: x.startswith("error_"), data.keys()))) > 0:
    send_notification("/fail")
else:
    send_notification()
