defmodule ServerWeb.SwapView do
  use ServerWeb, :view

  def render("show.json", %{swaps: swaps}), do: swaps_json(swaps)

  def swaps_json(swaps) do
    %{swaps: swaps}
  end
end
