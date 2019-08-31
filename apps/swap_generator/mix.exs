defmodule SwapGenerator.Mixfile do
  use Mix.Project

  def project do
    [
      app: :swap_generator,
      version: "0.1.0",
      elixir: "~> 1.9",
      start_permanent: Mix.env() == :prod,
      deps: deps(),
      build_path: "../../_build",
      config_path: "../../config/config.exs",
      deps_path: "../../deps",
      lockfile: "../../mix.lock"
    ]
  end

  def application do
    [
      extra_applications: [:logger]
    ]
  end

  defp deps do
    [
      {:diet, "~> 0.1.0"}
    ]
  end
end
