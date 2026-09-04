# Security Policy

## Our approach

x-DevPocket is offline-first by design: no backend, no accounts, no network
calls, no telemetry. That significantly shrinks the attack surface, but the
app still handles sensitive developer data on-device — tokens, keys, hashes,
JWTs — so we take reports seriously.

## Supported versions

Only the latest release/build is supported with security fixes. There are no
older maintained branches at this time.

## Reporting a vulnerability

**Please do not open a public GitHub issue for security vulnerabilities.**

Instead, use one of the following:

- Open a [private security advisory](../../security/advisories/new) on this repository (preferred — GitHub notifies maintainers directly and keeps the report private until a fix ships).
- If that's not available to you, contact a maintainer directly through their GitHub profile.

When reporting, please include:

- A description of the vulnerability and its potential impact
- Steps to reproduce (or a proof-of-concept)
- The app version/commit you tested against
- Any suggested remediation, if you have one

## What to expect

- Acknowledgement of your report within a few days.
- An assessment of severity and, where applicable, a plan and rough timeline for a fix.
- Credit in the release notes, if you'd like it, once the fix ships.

## Out of scope

- Issues that require a rooted/jailbroken device or a compromised OS to exploit.
- Reports about third-party dependencies with no demonstrated impact on this app specifically (please report those upstream instead).
