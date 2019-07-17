defmodule ServerWeb.SwapController do
  use ServerWeb, :controller

  def show(conn, %{"seed" => seed}) do
    swaps = SwapGenerator.run(seed)
    render(conn, "show.json", swaps: swaps)
  end
end
