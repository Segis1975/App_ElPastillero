# Reglas de ProGuard. El proyecto no ofusca en release (minifyEnabled false),
# pero se deja el archivo para poder activarlo en el futuro.
-keep class es.ies.claudiomoyano.dam2.elpastillero.data.** { *; }
