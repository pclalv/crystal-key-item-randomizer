defmodule ServerWeb.SwapView do
  use ServerWeb, :view

  def render("show.json", %{swap: swap}), do: swap_json(swap)

  def swap_json(swap) do
    %{swap: swap}
  end
end
