import requests
import concurrent.futures
url = "http://localhost:8080/orders"
headers = {
    "Content-Type": "application/json"
}
payload_template = {
    "userId": "1",
    "itemIds": [1, 2, 3],
    "totalAmount": 100.00
}
def send_order_request(order_id):
    payload = payload_template.copy()
    payload["userId"] = str(order_id)
    response = requests.post(url, json=payload, headers=headers)
    return response.status_code, response.json()
num_requests = 1000
with concurrent.futures.ThreadPoolExecutor(max_workers=num_requests) as executor:
    futures = [executor.submit(send_order_request, i) for i in range(num_requests)]
    for future in concurrent.futures.as_completed(futures):
        try:
            status_code, response_data = future.result()
            print(f"Request completed with status code: {status_code}, Response: {response_data}")
        except Exception as e:
            print(f"Request failed with exception: {e}")

print("All requests completed.")