TLS-Certificate-Check

This is telegram bot for detect certificate expiration
- max certificate expiration 7 days
- verification has every day

example configuration:
```yaml

# list of CA certificate if is need
listCaCertificate:
  - "-----BEGIN CERTIFICATE-----\r\n...........\r\n-----END CERTIFICATE-----"


pathToFileDB: file.db #file for save domains

telegramBot:
  botName:  
  botToken:
  chatId:   # it is for group chatID for send notification

``` 
---
Commands of telegramBot:
---
   * /help - shows all commands. Use /help [command] for more info
--- 
   * /add - Add domains command. Use /add [domain] for add domain to list
---
   * /show - Show all domains, that can check. Use /show for getting list of domains
---
   * /delete - Delete domain command. Use /delete [domain] for deleting domain from list
---