defmodule CrystalKeyItemRandomizer.Item do
  @fields [:name, :location, :macro,]
  @enforce_keys @fields
  defstruct @fields
end
