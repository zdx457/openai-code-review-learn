curl -X POST \
        -H "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04ifQ.eyJhcGlfa2V5IjoiNjhlZWQwZGY1Y2ZhNDk3OThkMTQzMGVlZDI0OGYxYzMiLCJleHAiOjE3NzQzNDAzMjA5MzcsInRpbWVzdGFtcCI6MTc3NDMzODUyMDk0MX0.gaTyad2j9iHx6n0kE3OuPw_3vDVxfVd4VExIgcr-zzk
" \
        -H "Content-Type: application/json" \
        -H "User-Agent: Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)" \
        -d '{
          "model":"glm-4",
          "stream": "true",
          "messages": [
              {
                  "role": "user",
                  "content": "1+1"
              }
          ]
        }' \
  https://open.bigmodel.cn/api/paas/v4
