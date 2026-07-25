#!/usr/bin/env bash
BASE="http://localhost:8080"

echo "========================================"
echo "   PLANEJADOR DE VIAGENS"
echo "========================================"
echo ""

read -p "Titulo da viagem: " TITULO
read -p "Data de inicio (ex: 2026-08-01): " DATA_INI
read -p "Data de fim (ex: 2026-08-15): " DATA_FIM
read -p "Orcamento total (R$): " ORCAMENTO

TRIP_RESP=$(curl -s -X POST "$BASE/trips" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"$TITULO\",\"startDate\":\"$DATA_INI\",\"endDate\":\"$DATA_FIM\",\"budget\":$ORCAMENTO}")
TRIP_ID=$(echo "$TRIP_RESP" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TRIP_ID" ]; then
  echo "ERRO ao criar viagem:"
  echo "$TRIP_RESP"
  exit 1
fi

echo "Viagem criada!"
echo ""
echo "Adicione quantos destinos quiser (deixe cidade em branco para parar):"
while true; do
  read -p "  Cidade: " CIDADE
  [ -z "$CIDADE" ] && break
  read -p "  Pais: " PAIS
  read -p "  Ordem (1, 2, 3...): " ORDEM
  RESP=$(curl -s -X POST "$BASE/trips/$TRIP_ID/destinations" \
    -H "Content-Type: application/json" \
    -d "{\"city\":\"$CIDADE\",\"country\":\"$PAIS\",\"displayOrder\":$ORDEM}")
  if echo "$RESP" | grep -q '"id"'; then
    echo "  Destino adicionado!"
  else
    echo "  ERRO: $RESP"
  fi
done

echo ""
echo "=== GERANDO SEU ROTEIRO PERSONALIZADO ==="
echo ""
HTTP_CODE=$(curl -s -o /tmp/plan_resp.txt -w "%{http_code}" -X POST "$BASE/trips/$TRIP_ID/plan")
if [ "$HTTP_CODE" = "200" ]; then
  python3 -c "
import json
d = json.load(open('/tmp/plan_resp.txt'))
print(d['content'])
"
else
  echo "ERRO HTTP $HTTP_CODE:"
  cat /tmp/plan_resp.txt
fi
echo ""
echo "========================================"
echo "   BOA VIAGEM!"
echo "========================================"
