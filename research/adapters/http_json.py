"""Forward the Asteria JSON protocol to a configured HTTP endpoint.

Usage: ASTERIA_ADAPTER_COMMAND='["python3","-m","research.adapters.http_json"]'
The endpoint must implement research/ADAPTER.md; this is not a vendor API client.
"""
import json
import os
import sys
import urllib.request


def main():
    try:
        payload = json.load(sys.stdin)
        headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}
        token = os.environ.get('ASTERIA_MODEL_TOKEN')
        if token: headers['Authorization'] = f'Bearer {token}'
        request = urllib.request.Request(os.environ['ASTERIA_MODEL_ENDPOINT'],
                                         data=json.dumps(payload).encode(), headers=headers, method='POST')
        # No retries or redirect following: a redirect must not forward credentials.
        class NoRedirect(urllib.request.HTTPRedirectHandler):
            def redirect_request(self, req, fp, code, msg, headers, newurl): return None
        with urllib.request.build_opener(NoRedirect).open(request, timeout=float(os.environ.get('ASTERIA_HTTP_TIMEOUT', '540'))) as response:
            sys.stdout.buffer.write(response.read())
    except Exception:
        # Endpoint URLs and exception text can contain credentials. Log privately in
        # a custom adapter if needed; do not print them into research observations.
        sys.stderr.write('HTTP adapter failed; inspect the service privately.\n')
        sys.exit(1)


if __name__ == '__main__': main()
